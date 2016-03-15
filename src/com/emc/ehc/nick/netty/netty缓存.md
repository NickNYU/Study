http://my.oschina.net/andylucc/blog/636661?fromerr=5ObmLD1i



摘要 在JAVA NIO相关的组件中，ByteBuffer是除了Selector、Channel之外的另一个很重要的组件，它是直接和Channel打交道的缓冲区，通常场景或是从ByteBuffer写入Channel，或是从Channel读入Buffer；而在Netty中，被精心设计的ByteBuf则是Netty贯穿整个开发过程中的核心缓冲区，那么他们俩有什么区别呢？Netty对于缓冲区的设计对于高性能应用又带来了哪些值得借鉴的思路呢？本文在介绍ByteBuffer和ByteBuf基本概念的基础之上对两者进行对比，进而扩展介绍Netty中的ByteBuf大家族。
Netty ByteBuf ByteBuffer

在JAVA NIO相关的组件中，ByteBuffer是除了Selector、Channel之外的另一个很重要的组件，它是直接和Channel打交道的缓冲区，通常场景或是从ByteBuffer写入Channel，或是从Channel读入Buffer；而在Netty中，被精心设计的ByteBuf则是Netty贯穿整个开发过程中的核心缓冲区，那么他们俩有什么区别呢？Netty对于缓冲区的设计对于高性能应用又带来了哪些值得借鉴的思路呢？本文在介绍ByteBuffer和ByteBuf基本概念的基础之上对两者进行对比，进而扩展介绍Netty中的ByteBuf大家族。



JAVA NIO之ByteBuffer

JAVA NIO中，Channel作为通往具有I/O操作属性的实体的抽象，这里的I/O操作通常指readding/writing，而具有I/O操作属性的实体比如I/O设备、文件、网络套接字等等。光有Channel可不行，我们必须为他增加readding/writing的特性，因此JAVA NIO基于Channel扩展WritableByteChannel和ReadableByteChannel接口。由于本文的重点是ByteBuffer，因此我们对于Channel的设计就看到这里，因为有了WritableByteChannel和ReadableByteChannel之后，我们就可以对ByteBuffer进行操作啦，看看他们提供的两个接口：


public int read(ByteBuffer dst) throws IOException;
public int write(ByteBuffer src) throws IOException;
从上面的接口我们可以看到Channel和ByteBuffer之间发生的两个基本行为，即readding/writing。无论是对文件（FileChannel）还是对网络（SocketChannel）的读写，他们都会去实现这两个基本行为。好了，我们已经从总体上认识ByteBuffer在JAVA NIO所处的位置和担当的角色了，下面我们继续深入一点认识ByteBuffer。

ByteBuffer有四个重要的属性，分别为：mark、position、limit、capacity，和两个重要方法分别为：flip和clear。ByteBuffer的底层存储结构对于堆内存和直接内存分别表现为堆上的一个byte[]对象和直接内存上分配的一块内存区域。既然是一块内存区域，那么我们就可以对其进行基于字节的读和写，而ByteBuffer的四个int类型的属性则是指向这块区域的指针：

position:读写指针，代表当前读或写操作的位置，这个值总是小于等于limit的。

mark：在使用ByteBuffer的过程中，如果想要记住当前的position，则会将当前的position值给mark，让需要恢复的时候，再将mark的值给position。

capacity：代表这块内存区域的大小。

limit：初始的Buffer中，limit和capacity的值是相等的，通常在clear操作和flip操作的时候会对这个值进行操作，在clear操作的时候会将这个值和capacity的值设置为相等，当flip的时候会将当前的position的值给limit，我们可以总结在写的时候，limit的值代表最大的可写位置，在读的时候，limit的值代表最大的可读位置。clear是为了写作准备、flip是为了读做准备。



    ByteBuffer指针示意图

在JAVA NIO中，原生的ByteByffer家族成员很简单，主要是HeapByteBuffer、DirectByteBuffer和MappedByteBuffer：

HeapByteBuffer是基于堆上字节数组为存储结构的缓冲区。

DirectByteBuffer是基于直接内存上的内存区域为存储结构的缓冲区。

MappedByteBuffer主要是文件操作相关的，它提供了一种基于虚拟内存映射的机制，使得我们可以像操作文件一样来操作文件，而不需要每次将内容更新到文件之中，同时读写效率非常高。



Netty之ByteBuf

相比于ByteBuffer的读写指针position，ByteBuf提供了两个指针readerIndex和writeIndex来分别指向读的位置和写的位置，不需要每次为读写做准备，直接设置读写指针进行读写操作即可。我们看看处于中间状态的状态：



读写中间状态的Buffer

从开始到readerIndex指针之间的这块区域是可以被丢弃的区域，后面会讲到，readerIndex和writerIndex指针之间的区域是可以被读的，writerIndex和capacity指针之间的区域是可以写的区域。当writerIndex指针到达顶端之后，ByteBuf允许用户复用之前已经被读过的区域，调用discardReadBytes方法即可，对应于上面的状态，调用discardReadBytes之后的状态如下：



调用discardReadBytes之后回收可用区域

除了discardReadBytes方法之外，另外一个比较重要的方法就是clear了，clear即清除缓冲区的指针状态，回复到初始值，对应于中间状态的那张图，调用clear之后的状态如下：



调用clear之后，Buffer状态的指针状态得到了初始化



Netty ByteBuf的特点

这里想要比较两种Buffer，对比ByteBuffer得出ByteBuf的优点点，我们首先要做的就是总结ByteBuf的特点以及相比ByteBuffer，这个特点如何成为优点：

（1）ByteBuf读写指针

在ByteBuffer中，读写指针都是position，而在ByteBuf中，读写指针分别为readerIndex和writerIndex，直观看上去ByteBuffer仅用了一个指针就实现了两个指针的功能，节省了变量，但是当对于ByteBuffer的读写状态切换的时候必须要调用flip方法，而当下一次写之前，必须要将Buffe中的内容读完，再调用clear方法。每次读之前调用flip，写之前调用clear，这样无疑给开发带来了繁琐的步骤，而且内容没有读完是不能写的，这样非常不灵活。相比之下我们看看ByteBuf，读的时候仅仅依赖readerIndex指针，写的时候仅仅依赖writerIndex指针，不需每次读写之前调用对应的方法，而且没有必须一次读完的限制。



（2）ByteBuf引用计数

ByteBuf扩展了ReferenceCountered接口，这个接口定义的功能主要是引用计数：



ReferenceCountered接口定义

也就是所有对ByteBuf的实现，都要实现引用计数，Netty对Buffer资源进行了显式的管理，这部分要结合Netty的内存池技术理解，当Buffer引用+1的时候，需要调用retain来让refCnt+1，当Buffer引用数-1的时候需要调用release来让refCnt-1,当refCnt变为0的时候Netty为pooled和unpooled的不同buffer提供了不同的实现，通常对于非内存池的用法，Netty把Buffer的内存回收交给了垃圾回收器，对于内存池的用法，Netty对内存的回收实际上是回收到内存池内，以提供下一次的申请所使用，关于内存池这部分可以参考我之前的一篇文章。

（3）池化Buffer资源

由于Netty是一个NIO网络框架，因此对于Buffer的使用如果基于直接内存（DirectBuffer）实现的话，将会大大提高I/O操作的效率，然而DirectBuffer和HeapBuffer相比之下除了I/O操作效率高之外还有一个天生的缺点，即对于DirectBuffer的申请相比HeapBuffer效率更低，因此Netty结合引用计数实现了PolledBuffer，即池化的用法，当引用计数等于0的时候，Netty将Buffer回收致池中，在下一次申请Buffer的没某个时刻会被复用。Netty这样做的基本想法是我们花了很大的力气申请了一块内存，不能轻易让他被回收呀，能重复利用当然重复利用咯。

（3）ByteBuffer才能和Channel打交道

归根结底，站在NIO的立场上所有的缓冲区要想和Channel打交道，换句话说也就是从网络Channel读取数据的时候，都是从Channel到ByteBuffer，从缓冲区写的网上上的时候，都是从ByteBuffer到Channel。因此，当Netty监听到I/O读事件的时候，会将自己流从Channel读到ByteBuffer而不是ByteBuf，see below:

?
1
return in.read((ByteBuffer) internalNioBuffer().clear().position(index).limit(index + length));
上面是ByteBuf的其中一个具体的读实现，可以看出ByteBuf维护着一个内部的ByteBuffer，叫做internalNioBuffer。当需要将字节流写入网络的时候，需要将ByteBuf转换为ByteBuffer，see below:

?
1
2
3
4
5
6
7
8
 ByteBuffer tmpBuf;
    if (internal) {
        tmpBuf = internalNioBuffer();
    } else {
        tmpBuf = ByteBuffer.wrap(array);
    }
    return out.write((ByteBuffer) tmpBuf.clear().position(index).limit(index + length));
}
上面是ByteBuf的其中一个具体的写实现，在写之前，总会将ByteBuf变成ByteBuffer。

稍微总结下这一节，ByteBuf本身的设计，在指针方面用两个读写指针分别代表读和写指针，这样做减少了Buffer使用的难度和出错率，概念上去理解也比较简单。在Netty中，每个被申请的Buffer对于Netty来说都可能是很宝贵的资源，因此为了获得对于内存的申请与回收更多的控制权，Netty自己根据引用计数法去实现了内存的管理，另外配合精心设计的池化算法在更大程度上控制了内存的使用，虽然相比单纯的申请-使用-释放来说实现可被管理、可被池化的Buffer是略复杂的，但是能为Netty卓越的性能数据做一些贡献，这绝对是值得的。最后我们要理清概念，JAVA NIO中和Channel打交道的只能是ByteBuffer，Netty在读写之前都有做转换，因此不要搞混，ByteBuf还是ByteBuf，它不是ByteBuffer。



Netty的Buffer大家族

这一节介绍一下Netty的Buffer大家族，ByteBuf的家族是庞大的，但是我们可以理清套路来将他们归类一下，这样看起来就不会那么的复杂，Netty主要围绕着2*2的维度进行对Buffer的扩展，他们分别是：

DirectBuffer

HeapBuffer

PooledBuffer

UnPooledBuffer

最高层的抽象是ByteBuf，Netty首先根据直接内存和堆内存，将Buffer按照这两个方向去扩展，之后再分别对具体的直接内存和堆内存缓冲区按照是否池话这两个方向再进行扩展。除了这两个维度，Netty还扩展了基于Unsafe的Buffer，我们分别挑出一个比较典型的实现来进行介绍：

PooledHeapByteBuf：池化的基于堆内存的缓冲区。

PooledDirectByteBuf：池化的基于直接内存的缓冲区。

PooledUnsafeDirectByteBuf：池化的基于Unsafe和直接内存实现的缓冲区。

UnPooledHeapByteBuf：非池化的基于堆内存的缓冲区。

UnPooledDirectByteBuf：非池化的基于直接内存的缓冲区。

UnPooledUnsafeDirectByteBuf：非池化的基于Unsafe和直接内存实现的缓冲区。

除了上面这些，另外Netty的Buffer家族还有CompositeByteBuf、ReadOnlyByteBufferBuf、ThreadLocalDirectByteBuf等等，这里还要说一下UnsafeBuffer，当当前平台支持Unsafe的时候，我们就可以使用UnsafeBuffer，JAVA DirectBuffer的实现也是基于unsafe来对内存进行操作的，我们可以看到不同的地方是PooledUnsafeDirectByteBuf或UnPooledUnsafeDirectByteBuf维护着一个memoryAddress变量，这个变量代表着缓冲区的内存地址，在使用的过程中加上一个offer就可以对内存进行灵活的操作。总的来说，Netty围绕着ByteBuf及其父接口定义的行为分别从是直接内存还是使用堆内存，是池话还是非池化，是否支持Unsafe来对ByteBuf进行不同的扩展实现。
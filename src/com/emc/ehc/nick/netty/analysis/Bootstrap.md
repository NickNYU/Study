
bootstrap包

bootstrap包是Netty4代码里最简单的一个包，总共只有4个类：
Bootstrap继承结构
AbstractBootstrap是抽象类，有两个具体的实现，Bootstrap和ServerBootstrap：


	public abstract class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel> implements Cloneable {
	
	    private volatile EventLoopGroup group;
	    private volatile ChannelFactory<? extends C> channelFactory;
	    private volatile SocketAddress localAddress;
	    private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<ChannelOption<?>, Object>();
	    private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap<AttributeKey<?>, Object>();
	    private volatile ChannelHandler handler;
	    // ...
	}
ChannelFactory
AbstractBootstrap通过ChannelFactory创建Channel实例，</br>
Bootstrap.channel(channelClass)方法看起来好像是设置了一个Channel，</br>
但实际上只是设置了默认的ChannelFactory实现：

	public B channel(Class<? extends C> channelClass) {
	    if (channelClass == null) {
	        throw new NullPointerException("channelClass");
	    }
	    return channelFactory(new BootstrapChannelFactory<C>(channelClass));
	}

ChannelFactory 中用反射的方法来构建channel

    private static final class BootstrapChannelFactory<T extends Channel> implements ChannelFactory<T> {
        private final Class<? extends T> clazz;

        BootstrapChannelFactory(Class<? extends T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T newChannel() {
            try {
                return clazz.newInstance();
            } catch (Throwable t) {
                throw new ChannelException("Unable to create Channel from class " + clazz, t);
            }
        }
    }

<b>Bootstrap.connect()</b></p>
再来看Bootstrap类的connect()方法：
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        validate();
        return doConnect(remoteAddress, localAddress);
    }

connect()方法调用validate()方法看各个Part是否准备就绪，然后调用doConnect()方法：

    private ChannelFuture doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        final ChannelFuture regFuture = initAndRegister();
        final Channel channel = regFuture.channel();
        if (regFuture.cause() != null) {
            return regFuture;
        }

        final ChannelPromise promise = channel.newPromise();
        if (regFuture.isDone()) {
            doConnect0(regFuture, channel, remoteAddress, localAddress, promise);
        } else {
            regFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    doConnect0(regFuture, channel, remoteAddress, localAddress, promise);
                }
            });
        }

        return promise;
    }

<p><b>AbstractBootstrap.initAndRegister()</b></p>

    final ChannelFuture initAndRegister() {
        final Channel channel = channelFactory().newChannel();
        try {
            init(channel);
        } catch (Throwable t) {
            channel.unsafe().closeForcibly();
            return channel.newFailedFuture(t);
        }

        ChannelPromise regPromise = channel.newPromise();
        group().register(channel, regPromise);
        // ...
    }



package com.mgg.environmentcheck.annotation;

/**
 * Provider interface, base of other interface.
 *
 * @author Alex <a href="mailto:zhilong.liu@aliyun.com">Contact me.</a>
 * @version 1.0
 * @since 16/8/23 23:08
 */
public interface ClassLoaderService extends IProvider {
	Class<?> forName();
}

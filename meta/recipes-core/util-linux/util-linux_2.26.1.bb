MAJOR_VERSION = "2.26"
require util-linux.inc

# To support older hosts, we need to patch and/or revert
# some upstream changes.  Only do this for native packages.
OLDHOST = ""
OLDHOST_class-native = "file://util-linux-native.patch \
                        file://util-linux-native-qsort.patch \
			"

SRC_URI += "file://util-linux-ng-replace-siginterrupt.patch \
            file://util-linux-ng-2.16-mount_lock_path.patch \
            file://uclibc-__progname-conflict.patch \
            file://configure-sbindir.patch \
            file://fix-parallel-build.patch \
            ${OLDHOST} \
"
SRC_URI[md5sum] = "2308850946766677f3fabe0685e85de8"
SRC_URI[sha256sum] = "22dc1c957262e2cbdfb4d524a63d5cd4f219d3ac9b5eab570fc771076799bb6e"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=ms"

EXTRA_OECONF_class-native = "${SHARED_EXTRA_OECONF} \
                             --disable-fallocate \
			     --disable-use-tty-group \
"
EXTRA_OECONF_class-nativesdk = "${SHARED_EXTRA_OECONF} \
                                --disable-fallocate \
				--disable-use-tty-group \
"

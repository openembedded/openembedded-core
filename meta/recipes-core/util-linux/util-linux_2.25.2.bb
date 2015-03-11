MAJOR_VERSION = "2.25"
require util-linux.inc
PR = "r1"

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
            file://CVE-2014-9114.patch \
            ${OLDHOST} \
"

SRC_URI[md5sum] = "cab3d7be354000f629bc601238b629b3"
SRC_URI[sha256sum] = "e0457f715b73f4a349e1acb08cb410bf0edc9a74a3f75c357070f31f70e33cd6"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=ms"

EXTRA_OECONF_class-native = "${SHARED_EXTRA_OECONF} \
                             --disable-fallocate --disable-use-tty-group \
"
EXTRA_OECONF_class-nativesdk = "${SHARED_EXTRA_OECONF} \
                                --disable-fallocate --disable-use-tty-group \
"

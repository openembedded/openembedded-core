MAJOR_VERSION = "2.24"
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
            file://fix-configure.patch \
            file://fix-parallel-build.patch \
            file://util-linux-ensure-the-existence-of-directory-for-PAT.patch \
            file://CVE-2014-9114.patch \
            ${OLDHOST} \
"

SRC_URI[md5sum] = "3f191727a0d28f7204b755cf1b6ea0aa"
SRC_URI[sha256sum] = "1243d6c07f1c5b38aa4c3814c81a71c24cba7dafe08942916bf216a90a460ff0"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=ms"

EXTRA_OECONF_class-native = "${SHARED_EXTRA_OECONF} \
                             --disable-fallocate --disable-use-tty-group \
"
EXTRA_OECONF_class-nativesdk = "${SHARED_EXTRA_OECONF} \
                                --disable-fallocate --disable-use-tty-group \
"

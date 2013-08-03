MAJOR_VERSION = "2.23"
require util-linux.inc

# To support older hosts, we need to patch and/or revert
# some upstream changes.  Only do this for native packages.
OLDHOST = ""
OLDHOST_class-native = "file://util-linux-native.patch"

SRC_URI += "file://util-linux-ng-replace-siginterrupt.patch \
            file://util-linux-ng-2.16-mount_lock_path.patch \
            file://uclibc-__progname-conflict.patch \
            file://configure-sbindir.patch \
            file://fix-configure.patch \
            ${OLDHOST} \
"

SRC_URI[md5sum] = "39a02ad0b3b37824c394f40930b7aa38"
SRC_URI[sha256sum] = "6c5be3f7beec91b4893de14bbd722886fa2521be5bfa0fc079b749d0018633b1"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=as"
EXTRA_OECONF_class-native += "--disable-fallocate --disable-use-tty-group"
EXTRA_OECONF_class-nativesdk += "--disable-fallocate --disable-use-tty-group"

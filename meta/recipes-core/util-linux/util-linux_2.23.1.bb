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

SRC_URI[md5sum] = "6741eeaff93ff5a6bacdd3816bdd87c4"
SRC_URI[sha256sum] = "ad4a7831d7b27d0172996fd343e809716c2403b32a94e15194d8ea797223c4af"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=as"
EXTRA_OECONF_class-native += "--disable-fallocate --disable-use-tty-group"
EXTRA_OECONF_class-nativesdk += "--disable-fallocate --disable-use-tty-group"

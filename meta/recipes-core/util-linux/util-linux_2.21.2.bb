MAJOR_VERSION = "2.21"
PR = "r5"
require util-linux.inc

SRC_URI += "file://util-linux-ng-replace-siginterrupt.patch \
            file://util-linux-ng-2.16-mount_lock_path.patch \
            file://uclibc-__progname-conflict.patch \
            file://configure-sbindir.patch \
            file://mbsalign-license.patch \
"

SRC_URI[md5sum] = "b75b3cfecb943f74338382fde693c2c3"
SRC_URI[sha256sum] = "066f9d8e51bfabd809d266edcd54eefba1cdca57725b95c074fd47fe6fba3d30"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=as"
EXTRA_OECONF_virtclass-native += "--disable-fallocate --disable-use-tty-group"

MAJOR_VERSION = "2.22"
PR = "r3"
require util-linux.inc

SRC_URI += "file://util-linux-ng-replace-siginterrupt.patch \
            file://util-linux-ng-2.16-mount_lock_path.patch \
            file://uclibc-__progname-conflict.patch \
            file://configure-sbindir.patch \
            file://fix-configure.patch \
            file://mbsalign-license.patch \
"

SRC_URI[md5sum] = "3e379b4d8b9693948d751c154614c73e"
SRC_URI[sha256sum] = "7463a17a01a77cee36d8ce845d8148208f553c9abdd67b446324bf42968bc36d"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=as"
EXTRA_OECONF_class-native += "--disable-fallocate --disable-use-tty-group"

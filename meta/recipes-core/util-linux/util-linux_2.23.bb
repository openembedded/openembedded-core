MAJOR_VERSION = "2.23"
PR = "r0"
require util-linux.inc

SRC_URI += "file://util-linux-ng-replace-siginterrupt.patch \
            file://util-linux-ng-2.16-mount_lock_path.patch \
            file://uclibc-__progname-conflict.patch \
            file://configure-sbindir.patch \
            file://fix-configure.patch \
            file://0001-lib-loopdev-fix-loopcxt_check_size-to-work-with-blkd.patch \
            file://0001-losetup-use-warn_size-for-regular-files-only.patch \
"

SRC_URI[md5sum] = "7bd10387f1aa00efaa4b07dfa13215bc"
SRC_URI[sha256sum] = "19ee024b4c6678eaa928d38edc011c332b088e0ff06239575f6b7e00a1855959"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=as"
EXTRA_OECONF_class-native += "--disable-fallocate --disable-use-tty-group"

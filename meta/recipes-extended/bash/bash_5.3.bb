require bash.inc

# GPL-2.0-or-later (< 4.0), GPL-3.0-or-later (>= 4.0)
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "${GNU_MIRROR}/bash/${BP}.tar.gz;name=tarball \
           file://mkbuiltins_have_stringize.patch \
           file://build-tests.patch \
           file://test-output.patch \
           file://run-ptest \
           file://run-bash-ptests \
           file://fix-run-builtins.patch \
           "

SRC_URI[tarball.sha256sum] = "62dd49c44c399ed1b3f7f731e87a782334d834f08e098a35f2c87547d5dbb269"

DEBUG_OPTIMIZATION:append:armv4 = " ${@bb.utils.contains('TUNE_CCARGS', '-mthumb', '-fomit-frame-pointer', '', d)}"
DEBUG_OPTIMIZATION:append:armv5 = " ${@bb.utils.contains('TUNE_CCARGS', '-mthumb', '-fomit-frame-pointer', '', d)}"

CFLAGS += "-std=gnu17"
# mkbuiltins.c is built with native toolchain and needs gnu17 as well:
# http://errors.yoctoproject.org/Errors/Details/853016/
BUILD_CFLAGS += "-std=gnu17"

BBCLASSEXTEND = "nativesdk"

SUMMARY = "A suite of basic system administration utilities"
HOMEPAGE = "https://en.wikipedia.org/wiki/Util-linux"
DESCRIPTION = "Util-linux includes a suite of basic system administration utilities \
commonly found on most Linux systems.  Some of the more important utilities include \
disk partitioning, kernel message management, filesystem creation, and system login."

SECTION = "base"

FILESEXTRAPATHS:prepend := "${THISDIR}/util-linux:"
MAJOR_VERSION = "${@'.'.join(d.getVar('PV').split('.')[0:2])}"
SRC_URI = "${KERNELORG_MIRROR}/linux/utils/util-linux/v${MAJOR_VERSION}/util-linux-${PV}.tar.xz \
           file://configure-sbindir.patch \
           file://runuser.pamd \
           file://runuser-l.pamd \
           file://ptest.patch \
           file://run-ptest \
           file://display_testname_for_subtest.patch \
           file://avoid_parallel_tests.patch \
           file://fcntl-lock.c \
           file://0001-tests-ts-kill-decode-avoid-using-shell-built-in-kill.patch \
           file://0001-lsfd-mkfds-foreign-sockets-skip-when-lacking-sock_di.patch \
           file://0001-ts-kill-decode-use-RTMIN-from-kill-L-instead-of-hard.patch \
           file://0001-tests-helpers-test_sigstate.c-explicitly-reset-SIGIN.patch \
           file://0001-include-mount-api-utils-avoid-using-sys-mount.h.patch \
           "

SRC_URI[sha256sum] = "be9ad9a276f4305ab7dd2f5225c8be1ff54352f565ff4dede9628c1aaa7dec57"

CVE_PRODUCT = "util-linux"

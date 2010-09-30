MAJOR_VERSION = "2.17"
require util-linux.inc
PR="r1"

SRC_URI += "file://uclibc-compile.patch \
	          file://util-linux-ng-replace-siginterrupt.patch"

# fallocate is glibc 2.10, fallocate64 is glibc 2.11
# we need to disable it for older versions
EXTRA_OECONF += "ac_cv_func_fallocate=no"
EXTRA_OECONF_virtclass-native += "--disable-fallocate --disable-use-tty-group"

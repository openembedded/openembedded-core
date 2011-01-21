MAJOR_VERSION = "2.17"
require util-linux.inc
PR = "r4"

SRC_URI += "file://uclibc-compile.patch \
	          file://util-linux-ng-replace-siginterrupt.patch"

SRC_URI[md5sum] = "4635725a3eef1c57090bac8ea5e082e6"
SRC_URI[sha256sum] = "c9ae801b6a5ab20b7749a278a8bf6830ef53adc5e8b7eb0ac1a9f410c774118f"

# fallocate is glibc 2.10, fallocate64 is glibc 2.11
# we need to disable it for older versions
EXTRA_OECONF += "ac_cv_func_fallocate=no"
EXTRA_OECONF_virtclass-native += "--disable-fallocate --disable-use-tty-group"

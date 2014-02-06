LICENSE = "GPLv2 & GPLv3 & LGPLv2 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
		    file://COPYING3;md5=d32239bcb673463ab874e80d47fae504 \
		    file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6 \
		    file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674"

SRC_URI += " file://gdb-fix-cygwin-check-in-configure-script.patch "

S = "${WORKDIR}/${BPN}-${PV}"

SRC_URI[md5sum] = "9ebf09fa76e4ca6034157086e9b20348"
SRC_URI[sha256sum] = "17f7bcda1b24336aadcb1c5e703c355551e59467e221c6f4353b720dffad73a7"

LICENSE="GPLv2 & GPLv3 & LGPLv2 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
		    file://COPYING3;md5=d32239bcb673463ab874e80d47fae504 \
		    file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6 \
		    file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674"

SRC_URI += " file://gdb-fix-cygwin-check-in-configure-script.patch "

S = "${WORKDIR}/${BPN}-${PV}"

SRC_URI[md5sum] = "d42841167fd061d90fddf9a7212a1f9f"
SRC_URI[sha256sum] = "3ccf7f618536f89648ba89e322fc3c0946bc15c03ca09c32fd9e71bbca68104b"

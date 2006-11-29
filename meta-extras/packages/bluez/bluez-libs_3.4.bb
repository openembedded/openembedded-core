DESCRIPTION = "Linux Bluetooth Stack Userland Libaries."
SECTION = "libs"
PRIORITY = "optional"
HOMEPAGE = "http://www.bluez.org"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "http://bluez.sourceforge.net/download/bluez-libs-${PV}.tar.gz"

inherit autotools pkgconfig

HEADERS = "bluetooth.h bnep.h cmtp.h hci.h hci_lib.h hidp.h l2cap.h rfcomm.h sco.h sdp.h sdp_lib.h"

do_stage() {
        oe_libinstall -a -so -C src libbluetooth ${STAGING_LIBDIR}

        install -d ${STAGING_INCDIR}/bluetooth/
        for f in ${HEADERS}
        do
		install -m 0644 include/$f ${STAGING_INCDIR}/bluetooth/$f
        done
}

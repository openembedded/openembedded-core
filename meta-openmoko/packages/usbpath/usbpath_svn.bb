DESCRIPTION = "Convert the physical locations of a USB device to/from its number"
AUTHOR = "Werner Almesberger <werner@openmoko.org>"
SECTION = "console/utils"
LICENSE = "GPL"
DEPENDS = "libusb"

PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.openmoko.org/trunk/src/host;module=usbpath;proto=http"

S = "${WORKDIR}/usbpath"

inherit autotools

do_stage () {
	autotools_stage_all
}


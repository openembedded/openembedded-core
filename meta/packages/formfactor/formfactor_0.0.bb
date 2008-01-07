DESCRIPTION = "Device formfactor information"
SECTION = "base"
PR = "r8"

SRC_URI = "file://config file://machconfig"
S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
	# Only install file if it has a contents
        install -d ${D}${sysconfdir}/formfactor/
        install -m 0644 ${S}/config ${D}${sysconfdir}/formfactor/
	if [ -s "${S}/machconfig" ]; then
	        install -m 0644 ${S}/machconfig ${D}${sysconfdir}/formfactor/
	fi
	
	install -d ${D}${sysconfdir}/gtk-2.0/
	. ${S}/machconfig

	if [ "$HAVE_TOUCHSCREEN" = "1" ]; then
		echo "gtk-touchscreen-mode = 1" > ${D}${sysconfdir}/gtk-2.0/gtkrc
	fi
}

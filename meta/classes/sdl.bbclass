#
# (C) Michael 'Mickey' Lauer <mickey@Vanille.de>
#

DEPENDS += "virtual/libsdl libsdl-mixer libsdl-image"

APPDESKTOP ?= "${PN}.desktop"
APPNAME ?= "${PN}"
APPIMAGE ?= "${PN}.png"

sdl_do_sdl_install() {
	install -d ${D}${palmtopdir}/bin
	install -d ${D}${palmtopdir}/pics
	install -d ${D}${palmtopdir}/apps/Games
	ln -sf ${bindir}/${APPNAME} ${D}${palmtopdir}/bin/${APPNAME}
	install -m 0644 ${APPIMAGE} ${D}${palmtopdir}/pics/${PN}.png

	if [ -e "${APPDESKTOP}" ]
	then
		echo ${APPDESKTOP} present, installing to palmtopdir...
		install -m 0644 ${APPDESKTOP} ${D}${palmtopdir}/apps/Games/${PN}.desktop
	else
		echo ${APPDESKTOP} not present, creating one on-the-fly...
		cat >${D}${palmtopdir}/apps/Games/${PN}.desktop <<EOF
[Desktop Entry]
Note=Auto Generated... this may be not what you want
Comment=${DESCRIPTION}
Exec=${APPNAME}
Icon=${APPIMAGE}
Type=Application
Name=${PN}
EOF
	fi
}

EXPORT_FUNCTIONS do_sdl_install
addtask sdl_install after do_compile before do_populate_staging

SECTION = "x11/games"
SECTION_${PN}-opie = "opie/games"

PACKAGES += "${PN}-opie"
RDEPENDS_${PN}-opie += "${PN}"
FILES_${PN}-opie = "${palmtopdir}"

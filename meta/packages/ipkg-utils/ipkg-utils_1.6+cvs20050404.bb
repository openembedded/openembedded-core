DESCRIPTION = "Itsy Package Manager utilities"
SECTION = "base"
PRIORITY = "optional"
LICENSE = "GPL"
CONFLICTS = "ipkg-link"
RDEPENDS = "python"
SRCDATE = "20050404"
PR = "r17"

SRC_URI = "${HANDHELDS_CVS};module=ipkg-utils \
           file://ipkg-utils-fix.patch;patch=1 \
           file://ipkg-py-sane-vercompare.patch;patch=1 \
           file://ipkg-py-tarfile.patch;patch=1 \
           file://ipkg-make-index-track-stamps.patch;patch=1 \
           file://fields_tweaks.patch;patch=1 "

S = "${WORKDIR}/ipkg-utils"

INSTALL = "ipkg-build ipkg-deb-unbuild ipkg-unbuild ipkg-compare-versions ipkg-upload ipkg-make-index ipkg-link ipkg.py ipkg-list-fields"

do_compile() {
	oe_runmake ipkg-compare-versions
}

do_install() {
	install -d ${D}${bindir}
	for i in ${INSTALL}
	do
		install -m 0755 $i ${D}${bindir}
	done
}


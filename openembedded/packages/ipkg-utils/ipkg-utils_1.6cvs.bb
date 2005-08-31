DESCRIPTION = "Itsy Package Manager utilities"
DEPENDS = ""
SECTION = "base"
PRIORITY = "optional"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"
LICENSE = "GPL"
CONFLICTS = "ipkg-link"
RDEPENDS = "python"
PV_append = "${CVSDATE}"
PR = "r9"

SRC_URI = "${HANDHELDS_CVS};module=ipkg-utils"
	   
S = "${WORKDIR}/ipkg-utils"

INSTALL = "ipkg-build ipkg-deb-unbuild ipkg-unbuild ipkg-compare-versions ipkg-upload ipkg-make-index ipkg-link ipkg.py"

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

#FIXME: ipkg-utils is not allowed to have packages or else a ipkg-native -> ipkg-utils -> ipkg-utils
#       recursive dependency is triggered. This has been fixed by installing the ipkg-link script in
#       a dedicated package.
#PACKAGES_prepend = "ipkg-link "
#FILES_ipkg-link = "${bindir}/ipkg-link"


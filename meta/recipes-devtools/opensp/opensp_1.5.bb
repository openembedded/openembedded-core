SUMMARY = "An SGML parser"
DESCRIPTION = "An SGML parser used by the OpenJade suite of utilities."
HOMEPAGE = "http://openjade.sourceforge.net"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=641ff1e4511f0a87044ad42f87cb1045"

PR = "r2"

# At -Os it encounters calls to some inline functions which are then
# not found in any other objects with gcc 4.5
FULL_OPTIMIZATION += "-O2"

SRC_URI = "${SOURCEFORGE_MIRROR}/openjade/OpenSP-${PV}.tar.gz \
           file://m4.patch \
           file://attributevalue.patch \
           file://rangmap-fix.patch \
           file://fix-docdir.patch"

SRC_URI[md5sum] = "87f56e79ae0c20397f4207d61d154303"
SRC_URI[sha256sum] = "987eeb9460185950e066e5db3b5fa531e53e213742b545288405552a5a7bb704"

S = "${WORKDIR}/OpenSP-${PV}"

inherit autotools gettext

EXTRA_OECONF_virtclass-native = "\
	--enable-default-catalog=${sysconfdir}/sgml/catalog \
	--enable-default-search-path=${datadir}/sgml \
	"

do_configure_prepend () {
	# Stale m4 macros cause a variety of problems
	rm -rf ${S}/m4/*
}

do_install_append() {
	# Set up symlinks to often-used alternate names. See
	# http://www.linuxfromscratch.org/blfs/view/stable/pst/opensp.html
	cd ${D}${libdir}
	ln -sf libosp.so libsp.so

	cd ${D}${bindir}
	for util in nsgmls sgmlnorm spam spcat spent sx; do
		ln -sf o$util $util	
	done
	ln -sf osx sgml2xml
}

do_install_append_virtclass-native() {
	for util in nsgmls sgmlnorm spam spcat spent sx; do
		create_cmdline_wrapper ${D}/${bindir}/$util \
		    -D ${sysconfdir}/sgml
	done
}

FILES_${PN} += "${datadir}/OpenSP/"

BBCLASSEXTEND = "native"

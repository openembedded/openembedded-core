SUMMARY = "XML C Parser Library and Toolkit"
DESCRIPTION = "The XML Parser Library allows for manipulation of XML files.  Libxml2 exports Push and Pull type parser interfaces for both XML and HTML.  It can do DTD validation at parse time, on a parsed document instance or with an arbitrary DTD.  Libxml2 includes complete XPath, XPointer and Xinclude implementations.  It also has a SAX like interface, which is designed to be compatible with Expat."
HOMEPAGE = "http://www.xmlsoft.org/"
BUGTRACKER = "http://bugzilla.gnome.org/buglist.cgi?product=libxml2"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://Copyright;md5=bb90c48926316d9af6e2d70ca7013ade \
                    file://hash.c;beginline=6;endline=15;md5=ce702952bfddd7aee22639a2d6b54136 \
                    file://list.c;beginline=4;endline=13;md5=cdbfa3dee51c099edb04e39f762ee907 \
                    file://trio.c;beginline=5;endline=14;md5=6c025753c86d958722ec76e94cae932e"

DEPENDS_virtclass-native = "python-native"
DEPENDS =+ "zlib"

SRC_URI = "ftp://xmlsoft.org/libxml2/libxml2-${PV}.tar.gz \
           file://libxml-64bit.patch"


inherit autotools pkgconfig binconfig

EXTRA_OECONF = "--without-python --without-debug --without-legacy --without-catalog --without-docbook --with-c14n"
EXTRA_OECONF_virtclass-native = "--with-python=${STAGING_BINDIR}/python --without-legacy --with-catalog --without-docbook --with-c14n"
EXTRA_OECONF_virtclass-nativesdk = "--with-python=${STAGING_BINDIR}/python --without-legacy --with-catalog --without-docbook --with-c14n"
EXTRA_OECONF_linuxstdbase = "--without-python --with-debug --with-legacy --with-catalog --with-docbook --with-c14n"

# required for pythong binding
export HOST_SYS
export BUILD_SYS
export STAGING_LIBDIR
export STAGING_INCDIR

export LDFLAGS += "-ldl"

python populate_packages_prepend () {
	# autonamer would call this libxml2-2, but we don't want that
	if d.getVar('DEBIAN_NAMES', True):
		d.setVar('PKG_libxml2', '${MLPREFIX}libxml2')
}

PACKAGES += "${PN}-utils"

FILES_${PN}-dev += "${bindir}/*-config ${libdir}/xml2Conf.sh"
FILES_${PN}-utils += "${bindir}/*"

BBCLASSEXTEND = "native nativesdk"

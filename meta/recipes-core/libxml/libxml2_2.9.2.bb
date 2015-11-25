SUMMARY = "XML C Parser Library and Toolkit"
DESCRIPTION = "The XML Parser Library allows for manipulation of XML files.  Libxml2 exports Push and Pull type parser interfaces for both XML and HTML.  It can do DTD validation at parse time, on a parsed document instance or with an arbitrary DTD.  Libxml2 includes complete XPath, XPointer and Xinclude implementations.  It also has a SAX like interface, which is designed to be compatible with Expat."
HOMEPAGE = "http://www.xmlsoft.org/"
BUGTRACKER = "http://bugzilla.gnome.org/buglist.cgi?product=libxml2"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://Copyright;md5=2044417e2e5006b65a8b9067b683fcf1 \
                    file://hash.c;beginline=6;endline=15;md5=96f7296605eae807670fb08947829969 \
                    file://list.c;beginline=4;endline=13;md5=cdbfa3dee51c099edb04e39f762ee907 \
                    file://trio.c;beginline=5;endline=14;md5=6c025753c86d958722ec76e94cae932e"

DEPENDS =+ "zlib"

SRC_URI = "ftp://xmlsoft.org/libxml2/libxml2-${PV}.tar.gz;name=libtar \
           file://libxml-64bit.patch \
           file://ansidecl.patch \
           file://runtest.patch \
           file://run-ptest \
           file://libxml2-CVE-2014-0191-fix.patch \
           file://python-sitepackages-dir.patch \
           file://libxml-m4-use-pkgconfig.patch \
           file://configure.ac-fix-cross-compiling-warning.patch \
           file://0001-CVE-2015-1819-Enforce-the-reader-to-run-in-constant-.patch \
           file://CVE-2015-7942.patch \
           file://CVE-2015-8035.patch \
           http://www.w3.org/XML/Test/xmlts20080827.tar.gz;name=testtar \
           file://72a46a519ce7326d9a00f0b6a7f2a8e958cd1675.patch \
           file://0001-threads-Define-pthread-definitions-for-glibc-complia.patch \
          "

SRC_URI[libtar.md5sum] = "9e6a9aca9d155737868b3dc5fd82f788"
SRC_URI[libtar.sha256sum] = "5178c30b151d044aefb1b08bf54c3003a0ac55c59c866763997529d60770d5bc"
SRC_URI[testtar.md5sum] = "ae3d1ebe000a3972afa104ca7f0e1b4a"
SRC_URI[testtar.sha256sum] = "96151685cec997e1f9f3387e3626d61e6284d4d6e66e0e440c209286c03e9cc7"

BINCONFIG = "${bindir}/xml2-config"

inherit autotools pkgconfig binconfig-disabled pythonnative ptest

RDEPENDS_${PN}-ptest += "python-core"

RDEPENDS_${PN}-python += "python-core"

RDEPENDS_${PN}-ptest_append_libc-glibc += "glibc-gconv-ebcdic-us glibc-gconv-ibm1141"

# We don't DEPEND on binutils for ansidecl.h so ensure we don't use the header
do_configure_prepend () {
	sed -i -e '/.*ansidecl.h.*/d' ${S}/configure.ac
}

export PYTHON_SITE_PACKAGES="${PYTHON_SITEPACKAGES_DIR}"

PACKAGECONFIG ??= "python"

PACKAGECONFIG[python] = "--with-python=${PYTHON},--without-python,python"
# WARNING: zlib is require for RPM use
EXTRA_OECONF = "--without-debug --without-legacy --with-catalog --without-docbook --with-c14n --without-lzma --with-fexceptions"
EXTRA_OECONF_class-native = "--without-legacy --without-docbook --with-c14n --without-lzma --with-zlib"
EXTRA_OECONF_class-nativesdk = "--without-legacy --without-docbook --with-c14n --without-lzma --with-zlib"
EXTRA_OECONF_linuxstdbase = "--with-debug --with-legacy --with-docbook --with-c14n --without-lzma --with-zlib"

# required for pythong binding
export HOST_SYS
export BUILD_SYS
export STAGING_LIBDIR
export STAGING_INCDIR

python populate_packages_prepend () {
    # autonamer would call this libxml2-2, but we don't want that
    if d.getVar('DEBIAN_NAMES', True):
        d.setVar('PKG_libxml2', '${MLPREFIX}libxml2')
}

PACKAGES += "${PN}-utils ${PN}-python"

FILES_${PN}-dbg += "${PYTHON_SITEPACKAGES_DIR}/.debug"
FILES_${PN}-staticdev += "${PYTHON_SITEPACKAGES_DIR}/*.a"
FILES_${PN}-dev += "${libdir}/xml2Conf.sh ${libdir}/cmake/*"
FILES_${PN}-utils += "${bindir}/*"
FILES_${PN}-python += "${PYTHON_SITEPACKAGES_DIR}"

do_install_ptest () {
  cp -r ${WORKDIR}/xmlconf ${D}${PTEST_PATH}
}

BBCLASSEXTEND = "native nativesdk"

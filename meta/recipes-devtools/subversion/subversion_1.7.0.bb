DESCRIPTION = "The Subversion (svn) client"
SECTION = "console/network"
DEPENDS = "apr-util neon"
RDEPENDS_${PN} = "neon"
LICENSE = "Apache-2"
HOMEPAGE = "http://subversion.tigris.org"

BBCLASSEXTEND = "native"

# negative, because of new checkout format in 1.7.0 
# and distro PREMIRRORs need to be in sync with users
DEFAULT_PREFERENCE = "-1"

SRC_URI = "${APACHE_MIRROR}/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://libtool2.patch \
"
SRC_URI[md5sum] = "930e6644a1b6094efd268fde6a318f04"
SRC_URI[sha256sum] = "64fd5f263a80e609717a3ca42f1f2625606a5c4a40a85716f82c866033780978"

LIC_FILES_CHKSUM = "file://LICENSE;md5=4a14fd2da3134e40a087eb4326a4ecd4"

EXTRA_OECONF = " \
                --without-berkeley-db --without-apxs --without-apache \
                --without-swig --with-apr=${STAGING_BINDIR_CROSS} \
                --with-apr-util=${STAGING_BINDIR_CROSS}"

inherit autotools

export LDFLAGS += " -L${STAGING_LIBDIR} "

acpaths = "-I build/ -I build/ac-macros/"

do_configure_prepend () {
	rm -f ${S}/libtool
	rm -f ${S}/build/libtool.m4
	sed -i -e 's:with_sasl="/usr/local":with_sasl="${STAGING_DIR}":' ${S}/build/ac-macros/sasl.m4
}

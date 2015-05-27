DEPENDS = "curl db"
RDEPENDS_${PN} = "dpkg bash debianutils"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
require apt.inc

SRC_URI[md5sum] = "e70c6d6227883cfc0dda6bc5db509bca"
SRC_URI[sha256sum] = "96bebcd7bfee0b2386741a8315182ba39487bdd743ecf5c1fc5b8b889cca2478"

require apt-package.inc

PACKAGECONFIG ??= "lzma"
PACKAGECONFIG[lzma] = "ac_cv_lib_lzma_lzma_easy_encoder=yes,ac_cv_lib_lzma_lzma_easy_encoder=no,xz"

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"

do_install_append() {
    #Write the correct apt-architecture to apt.conf
    APT_CONF=${D}/etc/apt/apt.conf
    echo 'APT::Architecture "${DPKG_ARCH}";' > ${APT_CONF}
    oe_libinstall -so -C bin libapt-private ${D}${libdir}/
}

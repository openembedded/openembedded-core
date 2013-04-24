DEPENDS = "curl db"
RDEPENDS_${PN} = "dpkg"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
require apt.inc

SRC_URI[md5sum] = "d44f459d59d8fa7fc5f455f1f982f08c"
SRC_URI[sha256sum] = "9570905992f4a83b0c182f11f9e0a8c20a1209a52996d1a01ddbfa359ae2c819"

require apt-package.inc

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"

do_install_append() {
    #Write the correct apt-architecture to apt.conf
    APT_CONF=${D}/etc/apt/apt.conf
    echo 'APT::Architecture "${DPKG_ARCH}";' > ${APT_CONF}
}

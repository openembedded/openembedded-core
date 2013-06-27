DEPENDS = "curl db"
RDEPENDS_${PN} = "dpkg"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
require apt.inc

SRC_URI[md5sum] = "956bb906224a5662111d353ab11a0347"
SRC_URI[sha256sum] = "cb0360e218490d875dc1e9e15c4e0ba0b53c7f512a6c98253f2eb07877be5106"

require apt-package.inc

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"

do_install_append() {
    #Write the correct apt-architecture to apt.conf
    APT_CONF=${D}/etc/apt/apt.conf
    echo 'APT::Architecture "${DPKG_ARCH}";' > ${APT_CONF}
}

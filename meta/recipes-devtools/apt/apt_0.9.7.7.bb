DEPENDS = "curl db"
RDEPENDS_${PN} = "dpkg"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
require apt.inc

PR = "r0"


SRC_URI[md5sum] = "039fc57668d1a0c2f62dc22e71900a16"
SRC_URI[sha256sum] = "6eca4285f1ac2e8cb837b9546553b0546881ed15853aa5bbeb860bab6bfa1700"

require apt-package.inc

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"

do_install_append() {
    #Write the correct apt-architecture to apt.conf
    APT_CONF=${D}/etc/apt/apt.conf
    echo 'APT::Architecture "${DPKG_ARCH}";' > ${APT_CONF}
}

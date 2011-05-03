SUMMARY = "Convert LinuxDoc SGML source into other formats"
DESCRIPTION = "Convert LinuxDoc SGML source into other formats"
HOMEPAGE = "http://packages.debian.org/linuxdoc-tools"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=5fbccc46cff2379505ca4e09c7d6ccfe"

DEPENDS = "groff-native openjade-native"

PR = "r3"

SRC_URI = "${DEBIAN_MIRROR}/main/l/linuxdoc-tools/linuxdoc-tools_${PV}.tar.gz \
           file://disable_sgml2rtf.patch \
           file://disable_txt_doc.patch \
           file://disable_tex_doc.patch \
           file://disable_dvips_doc.patch"

SRC_URI[md5sum] = "f214e79b0dd084689cd04f18722bd563"
SRC_URI[sha256sum] = "128cabb52ef8fb2f370ee488ea92bf4d8e49859200c7c8cae807abfe860a62ec"

inherit autotools native

do_configure () {
	oe_runconf
}

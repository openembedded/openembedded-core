DESCRIPTION = "createrepo creates rpm-metadata for rpms to build the repository"
HOMEPAGE = "http://createrepo.baseurl.org/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760"

PR = "r0"

SRC_URI= "http://createrepo.baseurl.org/download/${BP}.tar.gz \
          file://fix-native-install.patch \
         "
SRC_URI[md5sum] = "10641f19a40e9f633b300e23dde00349"
SRC_URI[sha256sum] = "ee897463837b299fb20bf6e970f8c5324cd8b7f26ad3675a9938a2d7ae42ff03"

BBCLASSEXTEND = "native"

do_install () {
	oe_runmake -e 'DESTDIR=${D}' install
}

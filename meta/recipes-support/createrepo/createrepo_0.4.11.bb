DESCRIPTION = "createrepo creates rpm-metadata for rpms to build the repository"
HOMEPAGE = "http://createrepo.baseurl.org/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760"

PR = "r2"

SRC_URI= "http://createrepo.baseurl.org/download/${BP}.tar.gz \
          file://fix-native-install.patch \
          file://python-scripts-should-use-interpreter-from-env.patch \
         "

SRC_URI[md5sum] = "3e9ccf4abcffe3f49af078c83611eda2"
SRC_URI[sha256sum] = "a73ae11a0dcde8bde36d900bc3f7f8f1083ba752c70a5c61b72d1e1e7608f21b"

BBCLASSEXTEND = "native"

do_install () {
	oe_runmake -e 'DESTDIR=${D}' install
}

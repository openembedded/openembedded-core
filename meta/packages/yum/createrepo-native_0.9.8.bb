DEPENDS = "python-native libxml2-native"
RDEPENDS = "yum-native"

SRC_URI = "http://createrepo.baseurl.org/download/createrepo-${PV}.tar.gz \
           file://pathfix.patch;patch=1 "
PR = "r2"

inherit autotools native

S = "${WORKDIR}/createrepo-${PV}"

do_compile_append () {
	sed -e 's#exec /usr/share#exec ${datadir}#' -i ${S}/bin/createrepo
	sed -e 's#exec /usr/share#exec ${datadir}#' -i ${S}/bin/modifyrepo
	sed -e 's#!/usr/bin/python#!${bindir}/python#' -i ${S}/genpkgmetadata.py
	sed -e 's#!/usr/bin/python#!${bindir}/python#' -i ${S}/modifyrepo.py
}

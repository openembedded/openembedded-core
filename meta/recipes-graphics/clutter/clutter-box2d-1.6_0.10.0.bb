require clutter-box2d.inc

PR = "r1"

DEPENDS += "clutter-1.6"

SRC_URI = "http://source.clutter-project.org/sources/clutter-box2d/0.10/clutter-box2d-${PV}.tar.bz2 \
           file://fix-disable-introspection.patch \
           file://isfinite.patch \
          "

S = "${WORKDIR}/clutter-box2d-${PV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

BASE_CONF += "--disable-introspection"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}

SRC_URI[md5sum] = "51618976ca6a5d536c4eac5f0e120d9d"
SRC_URI[sha256sum] = "1e42d0cea429e4dc953a1f652672dbd322b3938846e99bab35f463de6fd8ae7f"

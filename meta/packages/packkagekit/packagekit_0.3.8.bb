HOMEPAGE = "http://www.packagekit.org/"
PR = "r5"

SRC_URI = "http://www.packagekit.org/releases/PackageKit-0.3.8.tar.gz \
           file://no_validate.patch;patch=1 "

DEPENDS = "python polkit pam"
EXTRA_OECONF = "--disable-qt --disable-tests --enable-yum  --with-default-backend=yum --disable-local --with-security-framework=polkit"

S = "${WORKDIR}/PackageKit-${PV}"

inherit autotools pkgconfig

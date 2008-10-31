HOMEPAGE = "http://www.packagekit.org/"
PR = "r4"

SRC_URI = "http://www.packagekit.org/releases/PackageKit-0.3.8.tar.gz"

DEPENDS = "python polkit pam opkg-sdk"
EXTRA_OECONF = "--disable-qt --disable-tests --enable-yum  --with-default-backend=yum --disable-local"

S = "${WORKDIR}/PackageKit-${PV}"

inherit autotools pkgconfig

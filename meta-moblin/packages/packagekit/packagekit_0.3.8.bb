HOMEPAGE = "http://www.packagekit.org/"
PR = "r7"

SRC_URI = "http://www.packagekit.org/releases/PackageKit-0.3.8.tar.gz \
           file://no_validate.patch;patch=1 "

DEPENDS = "python policykit pam"
EXTRA_OECONF = "--disable-qt --disable-tests --enable-yum  --with-default-backend=yum --disable-local --with-security-framework=polkit --disable-gstreamer-plugin --disable-browser-plugin"

S = "${WORKDIR}/PackageKit-${PV}"

inherit autotools pkgconfig

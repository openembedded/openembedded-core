DESCRIPTION = "Advanced front-end for dpkg."
LICENSE = "GPL"
SECTION = "base"
DEPENDS = "curl db"
RDEPENDS = "dpkg"

SRC_URI = "${DEBIAN_MIRROR}/main/a/apt/apt_${PV}.tar.gz \
           file://environment.patch;patch=1"
S = "${WORKDIR}/apt-${PV}"

inherit autotools gettext

EXTRA_AUTORECONF = "--exclude=autopoint,autoheader"

# Apt wants to know the glibc version by running a binary file, which will
# fail, so we have to tell configure which version to use Since I don't know
# the impliations of setting a wrong value I only provide one for angstrom,
# which uses glibc 2.5 (which claims to be 2.4) 
# Koen - 20070327
EXTRA_OECONF_append_angstrom = " ac_cv_glibc_ver=libc6.4"

# under Debian it is set to libc6.3 as they use glibc 2.3
# They also provide glibc 2.5 in 'experimental' and it works with APT built
# for 2.3 so we set it in same way
EXTRA_OECONF_append = " ac_cv_glibc_ver=libc6.3"

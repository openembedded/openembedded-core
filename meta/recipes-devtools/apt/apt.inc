SUMMARY = "Advanced front-end for dpkg"
LICENSE = "GPLv2.0+"
SECTION = "base"

SRC_URI = "${DEBIAN_MIRROR}/main/a/apt/apt_${PV}.tar.gz \
           file://no-ko-translation.patch \
           file://use-host.patch \
           file://makerace.patch \
           file://no-nls-dpkg.patch \
           file://fix-gcc-4.6-null-not-defined.patch \
           file://truncate-filename.patch \
           file://nodoc.patch \
           file://disable-configure-in-makefile.patch \
           file://apt-0.9.9.4-CVE-2014-0478.patch \
           "

inherit autotools gettext

EXTRA_AUTORECONF = "--exclude=autopoint,autoheader"

do_configure_prepend() {
    rm -rf ${S}/buildlib/config.sub
    rm -rf ${S}/buildlib/config.guess
}

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

FILES_${PN}-dbg += "${libdir}/apt/methods/.debug/"

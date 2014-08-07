SUMMARY = "A library for text mode user interfaces"

DESCRIPTION = "Newt is a programming library for color text mode, widget based user \
interfaces.  Newt can be used to add stacked windows, entry widgets, \
checkboxes, radio buttons, labels, plain text fields, scrollbars, \
etc., to text mode user interfaces.  This package also contains the \
shared library needed by programs built with newt, as well as a \
/usr/bin/dialog replacement called whiptail.  Newt is based on the \
slang library."

HOMEPAGE = "https://fedorahosted.org/newt/"
SECTION = "libs"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"

# slang needs to be >= 2.2
DEPENDS = "slang popt"

PR = "r2"

SRC_URI = "https://fedorahosted.org/releases/n/e/newt/newt-${PV}.tar.gz \
           file://remove_slang_include.patch \
           file://fix_SHAREDDIR.patch \
           file://cross_ar.patch \
"

SRC_URI[md5sum] = "f36d4d908965a0c89fd6fd8b61a6118b"
SRC_URI[sha256sum] = "69837973ef2ee2fa644426f1c3e48d2b18785ebcd382ef7fd01eb2e67d2d632b"

S = "${WORKDIR}/newt-${PV}"

EXTRA_OECONF = "--without-tcl --without-python"

inherit autotools-brokensep

export STAGING_INCDIR
export STAGING_LIBDIR

export BUILD_SYS
export HOST_SYS

PACKAGES_prepend = "whiptail "

do_configure_prepend() {
    sh autogen.sh
}

do_compile_prepend() {
    # Make sure the recompile is OK
    rm -f ${B}/.depend
}

FILES_whiptail = "${bindir}/whiptail"

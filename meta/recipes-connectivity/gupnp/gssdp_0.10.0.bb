SUMMARY = "Resource discovery and announcement over SSDP"
DESCRIPTION = "GSSDP implements resource discovery and announcement over SSDP (Simpe Service Discovery Protocol)."
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"
DEPENDS = "glib-2.0 libsoup-2.4 libglade"

SRC_URI = "http://gupnp.org/sites/all/files/sources/${BPN}-${PV}.tar.gz \
           file://introspection.patch"

SRC_URI[md5sum] = "725c32e8f92a072cc34f0e091937df2a"
SRC_URI[sha256sum] = "8eaab799f699836770ec2fcc08abfef2f824a82ae959c6af7b39ffb6968b9fd7"

inherit autotools pkgconfig

PACKAGES =+ "gssdp-tools"

FILES_gssdp-tools = "${bindir}/gssdp* ${datadir}/gssdp/*.glade"

EXTRA_OECONF = "--disable-introspection"

PR = "r1"

SRC_URI[md5sum] = "aec6a56ac1d4f8a4837da83f2d152556"
SRC_URI[sha256sum] = "94de92bb4f7906ed2f047b0146a3b21d53d09908fe1f0149484f61c6afc598ea" 



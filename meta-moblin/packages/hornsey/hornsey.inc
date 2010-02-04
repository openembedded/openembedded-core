DESCRIPTION = "The Moblin Media Player"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"

SRC_URI = "http://download.moblin.org/sources/hornsey/1.0/hornsey-1.0_20091116.tar.bz2"

DEPENDS = "clutter-1.0 clutter-gtk-0.10 clutter-gst-0.10 bickley bognor-regis nbtk startup-notification libunique xtst"

EXTRA_OECONF = "--disable-shave"

FILES_${PN} += "${datadir}/icons"

inherit autotools_stage

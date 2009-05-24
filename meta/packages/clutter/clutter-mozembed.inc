DESCRIPTION = "Clutter Mozilla Embedded"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPL"

DEPENDS = "mozilla-headless mozilla-headless-services clutter"

FILESPATH = "${FILE_DIRNAME}/clutter-mozembed"

FILES_${PN} =+ "${datadir}/cluttermozembed/chrome/"

EXTRA_OECONF = "--enable-plugins"

inherit autotools_stage pkgconfig gtk-doc


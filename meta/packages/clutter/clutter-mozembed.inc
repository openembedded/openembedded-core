DESCRIPTION = "Clutter Mozilla Embedded"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPL"

DEPENDS = "mozilla-headless mozilla-headless-services"

FILESPATH = "${FILE_DIRNAME}/clutter-mozembed"

inherit autotools_stage pkgconfig gtk-doc


DESCRIPTION = "Aaina Clutter Demo"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "GPL"

DEPENDS = "clutter-0.6 libxml2 gtk+ neon"

inherit autotools pkgconfig

do_stage () {
        autotools_stage_all
}


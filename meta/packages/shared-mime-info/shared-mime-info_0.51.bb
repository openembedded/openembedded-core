require shared-mime-info.inc

DEPENDS = "libxml2 intltool-native glib-2.0 shared-mime-info-native"
PR = "r1"

do_install_append() {
    update-mime-database ${D}${datadir}/mime

    # we do not need it on device and it is huge
    rm ${D}${datadir}/mime/packages/freedesktop.org.xml
}

DEFAULT_PREFERENCE = "-1"

SECTION = "x11/utils"
DEPENDS = "gtk+ glib-2.0 gconf dbus libxml2 zlib bzip2 hal gnome-mime-data"
DESCRIPTION = "Virtual file system library using DBUS for communication."
LICENSE = "GPLv2"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
PROVIDES = "gnome-vfs"
RPROVIDES_${PN} = "gnome-vfs"
RPROVIDES_${PN}-dev = "gnome-vfs-dev"
RRECOMMENDS_${PN} = "gnome-vfs-plugin-file shared-mime-info"

PV = "2.12.0cvs${CVSDATE}"
PR = "r3"

SRC_URI = "svn://anonymous@developer.imendio.com/svn/gnome-vfs-dbus;module=trunk;proto=http \
	   file://no-gtk-doc.patch;patch=1 \
	   file://gssapi.patch;patch=1"
S = "${WORKDIR}/trunk"

inherit pkgconfig autotools

EXTRA_OECONF = "--with-ipc=dbus --enable-hal --disable-gtk-doc --disable-more-warnings --disable-howl"

FILES_${PN} += " ${libdir}/vfs ${datadir}/dbus-1/services/dbus-vfs-daemon.service"
FILES_${PN}-dev += " ${libdir}/gnome-vfs-2.0/modules/*.a ${libdir}/gnome-vfs-2.0/modules/*.la ${libdir}/gnome-vfs-2.0/include"
FILES_${PN}-doc += " ${datadir}/gtk-doc"

# These header lists have been copy-pasted from trunk/libgnomevfs/Makefile.am
GNOME_VFS_PLATFORM_HEADERS = " \
	gnome-vfs-file-size.h"

GNOME_VFS_MODULE_HEADERS = " \
        gnome-vfs-cancellable-ops.h             \
        gnome-vfs-inet-connection.h             \
        gnome-vfs-method.h                      \
        gnome-vfs-mime.h                        \
        gnome-vfs-mime-info.h                   \
        gnome-vfs-module-callback-module-api.h  \
        gnome-vfs-module-shared.h               \
        gnome-vfs-module.h                      \
        gnome-vfs-parse-ls.h                    \
        gnome-vfs-ssl.h                         \
        gnome-vfs-transform.h                   \
        gnome-vfs-socket-buffer.h               \
        gnome-vfs-socket.h"

GNOME_VFS_HEADERS = " \
        gnome-vfs-application-registry.h        \
        gnome-vfs-address.h                     \
        gnome-vfs-async-ops.h                   \
        gnome-vfs-cancellation.h                \
        gnome-vfs-context.h                     \
        gnome-vfs-directory.h                   \
        gnome-vfs-dns-sd.h                      \
        gnome-vfs-drive.h                       \
        gnome-vfs-enum-types.h                  \
        gnome-vfs-file-info.h                   \
        gnome-vfs-file-size.h                   \
        gnome-vfs-find-directory.h              \
        gnome-vfs-handle.h                      \
        gnome-vfs-init.h                        \
        gnome-vfs-job-limit.h                   \
        gnome-vfs-mime-deprecated.h             \
        gnome-vfs-mime-handlers.h               \
        gnome-vfs-mime-info-cache.h             \
        gnome-vfs-mime-monitor.h                \
        gnome-vfs-mime-utils.h                  \
        gnome-vfs-module-callback.h             \
        gnome-vfs-monitor.h                     \
        gnome-vfs-ops.h                         \
        gnome-vfs-resolve.h                     \
        gnome-vfs-result.h                      \
        gnome-vfs-standard-callbacks.h          \
        gnome-vfs-types.h                       \
        gnome-vfs-uri.h                         \
        gnome-vfs-utils.h                       \
        gnome-vfs-volume-monitor.h              \
        gnome-vfs-volume.h                      \
        gnome-vfs-xfer.h                        \
        gnome-vfs.h"

do_stage() {
        oe_libinstall -so -C libgnomevfs libgnomevfs-2 ${STAGING_LIBDIR}
        install -d ${STAGING_INCDIR}/gnome-vfs-2.0/libgnomevfs
	for i in ${GNOME_VFS_HEADERS}; do install -m 0644 libgnomevfs/$i ${STAGING_INCDIR}/gnome-vfs-2.0/libgnomevfs/; done
        install -d ${STAGING_INCDIR}/gnome-vfs-module-2.0/libgnomevfs
	for i in ${GNOME_VFS_MODULE_HEADERS}; do install -m 0644 libgnomevfs/$i ${STAGING_INCDIR}/gnome-vfs-module-2.0/libgnomevfs/; done
	install -d ${STAGING_INCDIR}/include/libgnomevfs
	for i in ${GNOME_VFS_PLATFORM_HEADERS}; do install -m 0644 libgnomevfs/$i ${STAGING_INCDIR}/include/libgnomevfs/; done
}

python populate_packages_prepend () {
        print bb.data.getVar('FILES_gnome-vfs', d, 1)

        plugindir = bb.data.expand('${libdir}/gnome-vfs-2.0/modules/', d)
        do_split_packages(d, plugindir, '^lib(.*)\.so$', 'gnome-vfs-plugin-%s',
'GNOME VFS plugin for %s')
}


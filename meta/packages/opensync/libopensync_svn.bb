LICENSE = "LGPL"
HOMEPAGE = "http://www.opensync.org/"
DEPENDS = "sqlite3 libxml2 zlib glib-2.0"
PV = "0.22+svn${SRCDATE}"

SRC_URI = "svn://svn.opensync.org;module=trunk;proto=http \
           file://fix-attr.patch;patch=1 \
	   file://gcc.patch;patch=1 \
	   file://zlib.patch;patch=1 \
	   file://no-werror.patch;patch=1"

inherit scons

S = "${WORKDIR}/trunk"

DEFAULT_PREFERENCE = "-1"

EXTRA_OECONF = "--disable-python"
LEAD_SONAME = "libopensync.so"

FILES_${PN} += " ${libdir}/opensync/formats/*.so ${datadir}/opensync/ ${libdir}/*.so"

export HOST_SYS = "${HOST_ARCH}${HOST_VENDOR}-${HOST_OS}"

do_stage() {
install -d ${STAGING_LIBDIR}/formats

install -m 0644 formats/libcontact.so ${STAGING_LIBDIR}/formats/
install -m 0644 formats/libdata.so ${STAGING_LIBDIR}/formats/
install -m 0644 formats/libevent.so ${STAGING_LIBDIR}/formats/
install -m 0644 formats/libfile.so ${STAGING_LIBDIR}/formats/
install -m 0644 formats/libopensync-*format.so ${STAGING_LIBDIR}/formats/
install -m 0644 formats/libtodo.so ${STAGING_LIBDIR}/formats/
install -m 0644 formats/libxmlformat-*.so ${STAGING_LIBDIR}/formats/
install -m 0644 opensync/libopensync.so ${STAGING_LIBDIR}

install -d ${STAGING_INCDIR}/opensync-1.0/opensync/archive/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/data/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/engine/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/format/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/group/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/helper/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/ipc/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/mapping/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/merger/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/plugin/
install -d ${STAGING_INCDIR}/opensync-1.0/opensync/version/

install -m 0644 formats/file.h	${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-client.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-context.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-data.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-engine.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-error.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-format.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-group.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-helper.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-ipc.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync_list.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-mapping.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-merger.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-module.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-plugin.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-serializer.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-support.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-time.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync-version.h ${STAGING_INCDIR}/opensync-1.0/opensync/
install -m 0644 opensync/opensync_xml.h ${STAGING_INCDIR}/opensync-1.0/opensync/

install -m 0644 opensync/archive/opensync_archive.h ${STAGING_INCDIR}/opensync-1.0/opensync/archive/
install -m 0644 opensync/data/opensync_change.h ${STAGING_INCDIR}/opensync-1.0/opensync/data/
install -m 0644 opensync/data/opensync_data.h ${STAGING_INCDIR}/opensync-1.0/opensync/data/
install -m 0644 opensync/engine/opensync_engine.h ${STAGING_INCDIR}/opensync-1.0/opensync/engine/
install -m 0644 opensync/engine/opensync_obj_engine.h ${STAGING_INCDIR}/opensync-1.0/opensync/engine/
install -m 0644 opensync/engine/opensync_status.h ${STAGING_INCDIR}/opensync-1.0/opensync/engine/
install -m 0644 opensync/format/opensync_converter.h ${STAGING_INCDIR}/opensync-1.0/opensync/format/
install -m 0644 opensync/format/opensync_filter.h ${STAGING_INCDIR}/opensync-1.0/opensync/format/
install -m 0644 opensync/format/opensync_format_env.h ${STAGING_INCDIR}/opensync-1.0/opensync/format/
install -m 0644 opensync/format/opensync_objformat.h ${STAGING_INCDIR}/opensync-1.0/opensync/format/
install -m 0644 opensync/format/opensync_time.h ${STAGING_INCDIR}/opensync-1.0/opensync/format/
install -m 0644 opensync/group/opensync_group_env.h ${STAGING_INCDIR}/opensync-1.0/opensync/group/
install -m 0644 opensync/group/opensync_group.h ${STAGING_INCDIR}/opensync-1.0/opensync/group/
install -m 0644 opensync/group/opensync_member.h ${STAGING_INCDIR}/opensync-1.0/opensync/group/
install -m 0644 opensync/helper/opensync_anchor.h ${STAGING_INCDIR}/opensync-1.0/opensync/helper/
install -m 0644 opensync/helper/opensync_hashtable.h ${STAGING_INCDIR}/opensync-1.0/opensync/helper/
install -m 0644 opensync/ipc/opensync_message.h ${STAGING_INCDIR}/opensync-1.0/opensync/ipc/
install -m 0644 opensync/ipc/opensync_queue.h ${STAGING_INCDIR}/opensync-1.0/opensync/ipc/
install -m 0644 opensync/ipc/opensync_serializer.h ${STAGING_INCDIR}/opensync-1.0/opensync/ipc/
install -m 0644 opensync/mapping/opensync_mapping_entry.h ${STAGING_INCDIR}/opensync-1.0/opensync/mapping/
install -m 0644 opensync/mapping/opensync_mapping.h ${STAGING_INCDIR}/opensync-1.0/opensync/mapping/
install -m 0644 opensync/mapping/opensync_mapping_table.h ${STAGING_INCDIR}/opensync-1.0/opensync/mapping/
install -m 0644 opensync/mapping/opensync_mapping_view.h ${STAGING_INCDIR}/opensync-1.0/opensync/mapping/
install -m 0644 opensync/merger/opensync_capabilities.h ${STAGING_INCDIR}/opensync-1.0/opensync/merger/
install -m 0644 opensync/merger/opensync_capability.h ${STAGING_INCDIR}/opensync-1.0/opensync/merger/
install -m 0644 opensync/merger/opensync_merger.h ${STAGING_INCDIR}/opensync-1.0/opensync/merger/
install -m 0644 opensync/merger/opensync_xmlfield.h ${STAGING_INCDIR}/opensync-1.0/opensync/merger/
install -m 0644 opensync/merger/opensync_xmlfieldlist.h ${STAGING_INCDIR}/opensync-1.0/opensync/merger/
install -m 0644 opensync/merger/opensync_xmlformat.h ${STAGING_INCDIR}/opensync-1.0/opensync/merger/
install -m 0644 opensync/plugin/opensync_plugin_env.h ${STAGING_INCDIR}/opensync-1.0/opensync/plugin/
install -m 0644 opensync/plugin/opensync_plugin.h ${STAGING_INCDIR}/opensync-1.0/opensync/plugin/
install -m 0644 opensync/plugin/opensync_plugin_info.h ${STAGING_INCDIR}/opensync-1.0/opensync/plugin/
install -m 0644 opensync/plugin/opensync_sink.h ${STAGING_INCDIR}/opensync-1.0/opensync/plugin/
install -m 0644 opensync/version/opensync_version.h ${STAGING_INCDIR}/opensync-1.0/opensync/version/

cat ${S}/opensync-1.0.pc | sed -e "s:=${WORKDIR}/image${libdir}:=${STAGING_LIBDIR}:" -e "s:=${WORKDIR}/image${includedir}:=${STAGING_INCDIR}:"  -e "s:=${WORKDIR}/image${datadir}:=${STAGING_DATADIR}:" > ${PKG_CONFIG_PATH}/opensync-1.0.pc
cat ${S}/osengine-1.0.pc | sed -e "s:=${WORKDIR}/image${libdir}:=${STAGING_LIBDIR}:" -e "s:=${WORKDIR}/image${includedir}:=${STAGING_INCDIR}:"  -e "s:=${WORKDIR}/image${datadir}:=${STAGING_DATADIR}:" > ${PKG_CONFIG_PATH}/osengine-1.0.pc

}

do_install_prepend() {
    install -d ${D}${libdir}
}

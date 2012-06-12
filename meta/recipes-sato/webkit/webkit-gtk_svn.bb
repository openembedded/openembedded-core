DESCRIPTION = "WebKitGTK+ is the port of the portable web rendering engine WebKitK to the GTK+ platform."
HOMEPAGE = "http://www.webkitgtk.org/"
BUGTRACKER = "http://bugs.webkit.org/"

LICENSE = "BSD & LGPLv2+"
LIC_FILES_CHKSUM = "file://Source/WebCore/rendering/RenderApplet.h;endline=22;md5=fb9694013ad71b78f8913af7a5959680 \
                    file://Source/WebKit/gtk/webkit/webkit.h;endline=21;md5=b4fbe9f4a944f1d071dba1d2c76b3351 \
                    file://Source/JavaScriptCore/parser/Parser.h;endline=23;md5=2f3cff0ad0a9c486da5a376928973a90"

# Choice of language backends - icu has issues on Big Endian machines so use pango
ICU_LIB = "icu"
ICU_LIB_powerpc = "pango"

DEPENDS = "zlib enchant gnome-keyring libsoup-2.4 curl libxml2 cairo libxslt libxt libidn gnutls \
           gtk+ gstreamer gst-plugins-base flex-native gperf-native perl-native-runtime sqlite3 ${ICU_LIB}"
DEPENDS += " ${@base_contains('DISTRO_FEATURES', 'opengl', 'virtual/libgl', '', d)}"

SRCREV_FORMAT = "source"

SRCREV = "101488"
PV = "1.7.2+svnr${SRCPV}"
PR = "r5"

SRC_URI = "\
  svn://svn.webkit.org/repository/webkit/trunk/;module=Source;proto=http;name=source \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKitLibraries;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=Tools;proto=http \
  file://Makefile \
  file://Makefile.shared \
  file://autogen.sh \
  file://configure.ac \
  file://GNUmakefile.am \
  file://gtk-doc.make \
  file://nodolt.patch \
  file://function-scope.patch \
 "

S = "${WORKDIR}/"

inherit autotools lib_package pkgconfig

EXTRA_OECONF = "\
                --enable-debug=no \
                --enable-svg \
                --enable-icon-database=yes \
                --enable-fullscreen-api \
                --enable-image-resizer \
                --enable-link-prefetch \
                --with-gtk=2.0 \
                ${@base_contains('DISTRO_FEATURES', 'opengl', '--enable-webgl', '--disable-webgl', d)} \
                UNICODE_CFLAGS=-D_REENTRANT \
               "

#default unicode backend icu breaks in cross-compile when target and host are different endian type
EXTRA_OECONF_append_powerpc += "--with-unicode-backend=glib"

CPPFLAGS_append_powerpc += "-I${STAGING_INCDIR}/pango-1.0 \
                            -I${STAGING_LIBDIR}/glib-2.0/include \
                            -I${STAGING_INCDIR}/glib-2.0"

EXTRA_AUTORECONF = " -I Source/autotools "


#| ./Source/JavaScriptCore/heap/HandleTypes.h: In static member function 'static T* JSC::HandleTypes<T>::getFromSlot(JSC::HandleSlot) [with T = JSC::Structure, JSC::HandleTypes<T>::ExternalType = JSC::Structure*, JSC::HandleSlot = JSC::JSValue*]':
#| ./Source/JavaScriptCore/heap/Handle.h:141:79:   instantiated from 'JSC::Handle<T>::ExternalType JSC::Handle<T>::get() const [with T = JSC::Structure, JSC::Handle<T>::ExternalType = JSC::Structure*]'
#| ./Source/JavaScriptCore/runtime/ScopeChain.h:39:75:   instantiated from here
#| ./Source/JavaScriptCore/heap/HandleTypes.h:38:130: warning: cast from 'JSC::JSCell*' to 'JSC::HandleTypes<JSC::Structure>::ExternalType {aka JSC::Structure*}' increases required alignment of target type [-Wcast-align]
#| {standard input}: Assembler messages:
#| {standard input}:28873: Error: invalid immediate: 983040 is out of range
#| {standard input}:28873: Error: value of 983040 too large for field of 2 bytes at 15110
#| /OE/shr-core/tmp/sysroots/x86_64-linux/usr/libexec/armv4t-oe-linux-gnueabi/gcc/arm-oe-linux-gnueabi/4.6.2/as: BFD (GNU Binutils) 2.21.1 assertion fail /OE/shr-core/tmp/work/armv4t-oe-linux-gnueabi/binutils-cross-2.21.1a-r0/binutils-2.21.1/bfd/elf.c:2819
#| arm-oe-linux-gnueabi-g++: internal compiler error: Segmentation fault (program as)
#| Please submit a full bug report,
#| with preprocessed source if appropriate.
#| See <http://gcc.gnu.org/bugs.html> for instructions.
#| make[1]: *** [Source/JavaScriptCore/jit/libjavascriptcoregtk_1_0_la-JIT.lo] Error 1
#| make[1]: Leaving directory `/OE/shr-core/tmp/work/armv4t-oe-linux-gnueabi/webkit-gtk-1.5.1+svnr90727-r0'
ARM_INSTRUCTION_SET = "arm"

CONFIGUREOPT_DEPTRACK = ""

do_configure_append() {
	# somethings wrong with icu, fix it up manually
	for makefile in $(find ${S} -name "GNUmakefile") ; do
		sed -i s:-I/usr/include::g $makefile
	done
}

do_install_prepend() {
	cp ${S}/Programs/.libs/jsc ${S}/Programs/jsc-1 || true
}

PACKAGES =+ "${PN}-webinspector ${PN}launcher-dbg ${PN}launcher libjavascriptcore"
FILES_${PN}launcher = "${bindir}/GtkLauncher"
FILES_${PN}launcher-dbg = "${bindir}/.debug/GtkLauncher"
FILES_libjavascriptcore = "${libdir}/libjavascriptcoregtk-1.0.so.*"
FILES_${PN}-webinspector = "${datadir}/webkitgtk-*/webinspector/"
FILES_${PN} += "${datadir}/webkitgtk-*/resources/error.html \
                ${datadir}/webkitgtk-*/images \
                ${datadir}/glib-2.0/schemas"



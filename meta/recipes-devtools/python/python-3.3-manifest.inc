
# WARNING: This file is AUTO GENERATED: Manual edits will be lost next time I regenerate the file.
# Generator: '../../../scripts/contrib/python/generate-manifest-3.3.py' Version 20140131 (C) 2002-2010 Michael 'Mickey' Lauer <mlauer@vanille-media.de>
# Visit the Python for Embedded Systems Site => http://www.Vanille.de/projects/python.spy

 

PROVIDES+="${PN}-2to3 ${PN}-audio ${PN}-codecs ${PN}-compile ${PN}-compression ${PN}-core ${PN}-crypt ${PN}-ctypes ${PN}-curses ${PN}-datetime ${PN}-db ${PN}-debugger ${PN}-dev ${PN}-difflib ${PN}-distutils ${PN}-distutils-staticdev ${PN}-doctest ${PN}-elementtree ${PN}-email ${PN}-fcntl ${PN}-gdbm ${PN}-html ${PN}-idle ${PN}-image ${PN}-io ${PN}-json ${PN}-lang ${PN}-logging ${PN}-mailbox ${PN}-math ${PN}-mime ${PN}-mmap ${PN}-multiprocessing ${PN}-netclient ${PN}-netserver ${PN}-numbers ${PN}-pickle ${PN}-pkgutil ${PN}-pprint ${PN}-profile ${PN}-pydoc ${PN}-re ${PN}-readline ${PN}-resource ${PN}-robotparser ${PN}-shell ${PN}-smtpd ${PN}-sqlite3 ${PN}-sqlite3-tests ${PN}-stringold ${PN}-subprocess ${PN}-syslog ${PN}-terminal ${PN}-tests ${PN}-textutils ${PN}-threading ${PN}-tkinter ${PN}-unittest ${PN}-unixadmin ${PN}-xml ${PN}-xmlrpc "

PACKAGES="${PN}-dbg ${PN}-2to3 ${PN}-audio ${PN}-codecs ${PN}-compile ${PN}-compression ${PN}-core ${PN}-crypt ${PN}-ctypes ${PN}-curses ${PN}-datetime ${PN}-db ${PN}-debugger ${PN}-dev ${PN}-difflib ${PN}-distutils-staticdev ${PN}-distutils ${PN}-doctest ${PN}-elementtree ${PN}-email ${PN}-fcntl ${PN}-gdbm ${PN}-html ${PN}-idle ${PN}-image ${PN}-io ${PN}-json ${PN}-lang ${PN}-logging ${PN}-mailbox ${PN}-math ${PN}-mime ${PN}-mmap ${PN}-multiprocessing ${PN}-netclient ${PN}-netserver ${PN}-numbers ${PN}-pickle ${PN}-pkgutil ${PN}-pprint ${PN}-profile ${PN}-pydoc ${PN}-re ${PN}-readline ${PN}-resource ${PN}-robotparser ${PN}-shell ${PN}-smtpd ${PN}-sqlite3 ${PN}-sqlite3-tests ${PN}-stringold ${PN}-subprocess ${PN}-syslog ${PN}-terminal ${PN}-tests ${PN}-textutils ${PN}-threading ${PN}-tkinter ${PN}-unittest ${PN}-unixadmin ${PN}-xml ${PN}-xmlrpc ${PN}-modules"

DESCRIPTION_${PN}-2to3="Python Automated Python 2 to 3 code translation"
RDEPENDS_${PN}-2to3="${PN}-core"
FILES_${PN}-2to3="${bindir}/2to3 ${libdir}/python3.3/lib2to3 "

DESCRIPTION_${PN}-audio="Python Audio Handling"
RDEPENDS_${PN}-audio="${PN}-core"
FILES_${PN}-audio="${libdir}/python3.3/wave.* ${libdir}/python3.3/chunk.* ${libdir}/python3.3/sndhdr.* ${libdir}/python3.3/lib-dynload/ossaudiodev.*.so ${libdir}/python3.3/lib-dynload/audioop.*.so ${libdir}/python3.3/audiodev.* ${libdir}/python3.3/sunaudio.* ${libdir}/python3.3/sunau.* ${libdir}/python3.3/toaiff.* "

DESCRIPTION_${PN}-codecs="Python Codecs, Encodings & i18n Support"
RDEPENDS_${PN}-codecs="${PN}-core ${PN}-lang"
FILES_${PN}-codecs="${libdir}/python3.3/codecs.* ${libdir}/python3.3/encodings ${libdir}/python3.3/gettext.* ${libdir}/python3.3/locale.* ${libdir}/python3.3/lib-dynload/_locale.*.so ${libdir}/python3.3/lib-dynload/_codecs* ${libdir}/python3.3/lib-dynload/_multibytecodec.*.so ${libdir}/python3.3/lib-dynload/unicodedata.*.so ${libdir}/python3.3/stringprep.* ${libdir}/python3.3/xdrlib.* "

DESCRIPTION_${PN}-compile="Python Bytecode Compilation Support"
RDEPENDS_${PN}-compile="${PN}-core"
FILES_${PN}-compile="${libdir}/python3.3/py_compile.* ${libdir}/python3.3/compileall.* "

DESCRIPTION_${PN}-compression="Python High Level Compression Support"
RDEPENDS_${PN}-compression="${PN}-core ${PN}-codecs"
FILES_${PN}-compression="${libdir}/python3.3/gzip.* ${libdir}/python3.3/zipfile.* ${libdir}/python3.3/tarfile.* ${libdir}/python3.3/lib-dynload/bz2.*.so "

DESCRIPTION_${PN}-core="Python Interpreter and core modules (needed!)"
RDEPENDS_${PN}-core="${PN}-lang ${PN}-re"
FILES_${PN}-core="${libdir}/python3.3/__future__.* ${libdir}/python3.3/_abcoll.* ${libdir}/python3.3/abc.* ${libdir}/python3.3/copy.* ${libdir}/python3.3/copy_reg.* ${libdir}/python3.3/ConfigParser.* ${libdir}/python3.3/genericpath.* ${libdir}/python3.3/getopt.* ${libdir}/python3.3/linecache.* ${libdir}/python3.3/new.* ${libdir}/python3.3/os.* ${libdir}/python3.3/posixpath.* ${libdir}/python3.3/struct.* ${libdir}/python3.3/warnings.* ${libdir}/python3.3/site.* ${libdir}/python3.3/stat.* ${libdir}/python3.3/UserDict.* ${libdir}/python3.3/UserList.* ${libdir}/python3.3/UserString.* ${libdir}/python3.3/lib-dynload/binascii.*.so ${libdir}/python3.3/lib-dynload/_struct.*.so ${libdir}/python3.3/lib-dynload/time.*.so ${libdir}/python3.3/lib-dynload/xreadlines.*.so ${libdir}/python3.3/types.* ${libdir}/python3.3/platform.* ${bindir}/python* ${libdir}/python3.3/_weakrefset.* ${libdir}/python3.3/sysconfig.* ${libdir}/python3.3/config/Makefile ${includedir}/python${PYTHON_MAJMIN}/pyconfig*.h ${libdir}/python${PYTHON_MAJMIN}/collections ${libdir}/python${PYTHON_MAJMIN}/sitecustomize.py "

DESCRIPTION_${PN}-crypt="Python Basic Cryptographic and Hashing Support"
RDEPENDS_${PN}-crypt="${PN}-core"
FILES_${PN}-crypt="${libdir}/python3.3/hashlib.* ${libdir}/python3.3/md5.* ${libdir}/python3.3/sha.* ${libdir}/python3.3/lib-dynload/crypt.*.so ${libdir}/python3.3/lib-dynload/_hashlib.*.so ${libdir}/python3.3/lib-dynload/_sha256.*.so ${libdir}/python3.3/lib-dynload/_sha512.*.so "

DESCRIPTION_${PN}-ctypes="Python C Types Support"
RDEPENDS_${PN}-ctypes="${PN}-core"
FILES_${PN}-ctypes="${libdir}/python3.3/ctypes ${libdir}/python3.3/lib-dynload/_ctypes.*.so ${libdir}/python3.3/lib-dynload/_ctypes_test.*.so "

DESCRIPTION_${PN}-curses="Python Curses Support"
RDEPENDS_${PN}-curses="${PN}-core"
FILES_${PN}-curses="${libdir}/python3.3/curses ${libdir}/python3.3/lib-dynload/_curses.*.so ${libdir}/python3.3/lib-dynload/_curses_panel.*.so "

DESCRIPTION_${PN}-datetime="Python Calendar and Time support"
RDEPENDS_${PN}-datetime="${PN}-core ${PN}-codecs"
FILES_${PN}-datetime="${libdir}/python3.3/_strptime.* ${libdir}/python3.3/calendar.* ${libdir}/python3.3/lib-dynload/datetime.*.so "

DESCRIPTION_${PN}-db="Python File-Based Database Support"
RDEPENDS_${PN}-db="${PN}-core"
FILES_${PN}-db="${libdir}/python3.3/anydbm.* ${libdir}/python3.3/dumbdbm.* ${libdir}/python3.3/whichdb.* ${libdir}/python3.3/dbm ${libdir}/python3.3/lib-dynload/_dbm.*.so "

DESCRIPTION_${PN}-debugger="Python Debugger"
RDEPENDS_${PN}-debugger="${PN}-core ${PN}-io ${PN}-lang ${PN}-re ${PN}-stringold ${PN}-shell ${PN}-pprint"
FILES_${PN}-debugger="${libdir}/python3.3/bdb.* ${libdir}/python3.3/pdb.* "

DESCRIPTION_${PN}-dev="Python Development Package"
RDEPENDS_${PN}-dev="${PN}-core"
FILES_${PN}-dev="${includedir} ${libdir}/lib*${SOLIBSDEV} ${libdir}/*.la ${libdir}/*.a ${libdir}/*.o ${libdir}/pkgconfig ${base_libdir}/*.a ${base_libdir}/*.o ${datadir}/aclocal ${datadir}/pkgconfig "

DESCRIPTION_${PN}-difflib="Python helpers for computing deltas between objects."
RDEPENDS_${PN}-difflib="${PN}-lang ${PN}-re"
FILES_${PN}-difflib="${libdir}/python3.3/difflib.* "

DESCRIPTION_${PN}-distutils="Python Distribution Utilities"
RDEPENDS_${PN}-distutils="${PN}-core"
FILES_${PN}-distutils="${libdir}/python3.3/config ${libdir}/python3.3/distutils "

DESCRIPTION_${PN}-distutils-staticdev="Python Distribution Utilities (Static Libraries)"
RDEPENDS_${PN}-distutils-staticdev="${PN}-distutils"
FILES_${PN}-distutils-staticdev="${libdir}/python3.3/config/lib*.a "

DESCRIPTION_${PN}-doctest="Python framework for running examples in docstrings."
RDEPENDS_${PN}-doctest="${PN}-core ${PN}-lang ${PN}-io ${PN}-re ${PN}-unittest ${PN}-debugger ${PN}-difflib"
FILES_${PN}-doctest="${libdir}/python3.3/doctest.* "

DESCRIPTION_${PN}-elementtree="Python elementree"
RDEPENDS_${PN}-elementtree="${PN}-core"
FILES_${PN}-elementtree="${libdir}/python3.3/lib-dynload/_elementtree.*.so "

DESCRIPTION_${PN}-email="Python Email Support"
RDEPENDS_${PN}-email="${PN}-core ${PN}-io ${PN}-re ${PN}-mime ${PN}-audio ${PN}-image ${PN}-netclient"
FILES_${PN}-email="${libdir}/python3.3/imaplib.* ${libdir}/python3.3/email "

DESCRIPTION_${PN}-fcntl="Python's fcntl Interface"
RDEPENDS_${PN}-fcntl="${PN}-core"
FILES_${PN}-fcntl="${libdir}/python3.3/lib-dynload/fcntl.*.so "

DESCRIPTION_${PN}-gdbm="Python GNU Database Support"
RDEPENDS_${PN}-gdbm="${PN}-core"
FILES_${PN}-gdbm="${libdir}/python3.3/lib-dynload/_gdbm.*.so "

DESCRIPTION_${PN}-html="Python HTML Processing"
RDEPENDS_${PN}-html="${PN}-core"
FILES_${PN}-html="${libdir}/python3.3/formatter.* ${libdir}/python3.3/htmlentitydefs.* ${libdir}/python3.3/htmllib.* ${libdir}/python3.3/markupbase.* ${libdir}/python3.3/sgmllib.* ${libdir}/python3.3/HTMLParser.* "

DESCRIPTION_${PN}-idle="Python Integrated Development Environment"
RDEPENDS_${PN}-idle="${PN}-core ${PN}-tkinter"
FILES_${PN}-idle="${bindir}/idle ${libdir}/python3.3/idlelib "

DESCRIPTION_${PN}-image="Python Graphical Image Handling"
RDEPENDS_${PN}-image="${PN}-core"
FILES_${PN}-image="${libdir}/python3.3/colorsys.* ${libdir}/python3.3/imghdr.* ${libdir}/python3.3/lib-dynload/imageop.*.so ${libdir}/python3.3/lib-dynload/rgbimg.*.so "

DESCRIPTION_${PN}-io="Python Low-Level I/O"
RDEPENDS_${PN}-io="${PN}-core ${PN}-math"
FILES_${PN}-io="${libdir}/python3.3/lib-dynload/_socket.*.so ${libdir}/python3.3/lib-dynload/_io.*.so ${libdir}/python3.3/lib-dynload/_ssl.*.so ${libdir}/python3.3/lib-dynload/select.*.so ${libdir}/python3.3/lib-dynload/termios.*.so ${libdir}/python3.3/lib-dynload/cStringIO.*.so ${libdir}/python3.3/pipes.* ${libdir}/python3.3/socket.* ${libdir}/python3.3/ssl.* ${libdir}/python3.3/tempfile.* ${libdir}/python3.3/StringIO.* ${libdir}/python3.3/io.* ${libdir}/python3.3/_pyio.* "

DESCRIPTION_${PN}-json="Python JSON Support"
RDEPENDS_${PN}-json="${PN}-core ${PN}-math ${PN}-re"
FILES_${PN}-json="${libdir}/python3.3/json ${libdir}/python3.3/lib-dynload/_json.*.so "

DESCRIPTION_${PN}-lang="Python Low-Level Language Support"
RDEPENDS_${PN}-lang="${PN}-core"
FILES_${PN}-lang="${libdir}/python3.3/lib-dynload/_bisect.*.so ${libdir}/python3.3/lib-dynload/_collections.*.so ${libdir}/python3.3/lib-dynload/_heapq.*.so ${libdir}/python3.3/lib-dynload/_weakref.*.so ${libdir}/python3.3/lib-dynload/_functools.*.so ${libdir}/python3.3/lib-dynload/array.*.so ${libdir}/python3.3/lib-dynload/itertools.*.so ${libdir}/python3.3/lib-dynload/operator.*.so ${libdir}/python3.3/lib-dynload/parser.*.so ${libdir}/python3.3/atexit.* ${libdir}/python3.3/bisect.* ${libdir}/python3.3/code.* ${libdir}/python3.3/codeop.* ${libdir}/python3.3/collections.* ${libdir}/python3.3/dis.* ${libdir}/python3.3/functools.* ${libdir}/python3.3/heapq.* ${libdir}/python3.3/inspect.* ${libdir}/python3.3/keyword.* ${libdir}/python3.3/opcode.* ${libdir}/python3.3/symbol.* ${libdir}/python3.3/repr.* ${libdir}/python3.3/token.* ${libdir}/python3.3/tokenize.* ${libdir}/python3.3/traceback.* ${libdir}/python3.3/weakref.* "

DESCRIPTION_${PN}-logging="Python Logging Support"
RDEPENDS_${PN}-logging="${PN}-core ${PN}-io ${PN}-lang ${PN}-pickle ${PN}-stringold"
FILES_${PN}-logging="${libdir}/python3.3/logging "

DESCRIPTION_${PN}-mailbox="Python Mailbox Format Support"
RDEPENDS_${PN}-mailbox="${PN}-core ${PN}-mime"
FILES_${PN}-mailbox="${libdir}/python3.3/mailbox.* "

DESCRIPTION_${PN}-math="Python Math Support"
RDEPENDS_${PN}-math="${PN}-core"
FILES_${PN}-math="${libdir}/python3.3/lib-dynload/cmath.*.so ${libdir}/python3.3/lib-dynload/math.*.so ${libdir}/python3.3/lib-dynload/_random.*.so ${libdir}/python3.3/random.* ${libdir}/python3.3/sets.* "

DESCRIPTION_${PN}-mime="Python MIME Handling APIs"
RDEPENDS_${PN}-mime="${PN}-core ${PN}-io"
FILES_${PN}-mime="${libdir}/python3.3/mimetools.* ${libdir}/python3.3/uu.* ${libdir}/python3.3/quopri.* ${libdir}/python3.3/rfc822.* ${libdir}/python3.3/MimeWriter.* "

DESCRIPTION_${PN}-mmap="Python Memory-Mapped-File Support"
RDEPENDS_${PN}-mmap="${PN}-core ${PN}-io"
FILES_${PN}-mmap="${libdir}/python3.3/lib-dynload/mmap.*.so "

DESCRIPTION_${PN}-multiprocessing="Python Multiprocessing Support"
RDEPENDS_${PN}-multiprocessing="${PN}-core ${PN}-io ${PN}-lang"
FILES_${PN}-multiprocessing="${libdir}/python3.3/lib-dynload/_multiprocessing.*.so ${libdir}/python3.3/multiprocessing "

DESCRIPTION_${PN}-netclient="Python Internet Protocol Clients"
RDEPENDS_${PN}-netclient="${PN}-core ${PN}-crypt ${PN}-datetime ${PN}-io ${PN}-lang ${PN}-logging ${PN}-mime"
FILES_${PN}-netclient="${libdir}/python3.3/*Cookie*.* ${libdir}/python3.3/base64.* ${libdir}/python3.3/cookielib.* ${libdir}/python3.3/ftplib.* ${libdir}/python3.3/gopherlib.* ${libdir}/python3.3/hmac.* ${libdir}/python3.3/httplib.* ${libdir}/python3.3/mimetypes.* ${libdir}/python3.3/nntplib.* ${libdir}/python3.3/poplib.* ${libdir}/python3.3/smtplib.* ${libdir}/python3.3/telnetlib.* ${libdir}/python3.3/urllib.* ${libdir}/python3.3/urllib2.* ${libdir}/python3.3/urlparse.* ${libdir}/python3.3/uuid.* ${libdir}/python3.3/rfc822.* ${libdir}/python3.3/mimetools.* "

DESCRIPTION_${PN}-netserver="Python Internet Protocol Servers"
RDEPENDS_${PN}-netserver="${PN}-core ${PN}-netclient"
FILES_${PN}-netserver="${libdir}/python3.3/cgi.* ${libdir}/python3.3/*HTTPServer.* ${libdir}/python3.3/SocketServer.* "

DESCRIPTION_${PN}-numbers="Python Number APIs"
RDEPENDS_${PN}-numbers="${PN}-core ${PN}-lang ${PN}-re"
FILES_${PN}-numbers="${libdir}/python3.3/decimal.* ${libdir}/python3.3/numbers.* "

DESCRIPTION_${PN}-pickle="Python Persistence Support"
RDEPENDS_${PN}-pickle="${PN}-core ${PN}-codecs ${PN}-io ${PN}-re"
FILES_${PN}-pickle="${libdir}/python3.3/pickle.* ${libdir}/python3.3/shelve.* ${libdir}/python3.3/lib-dynload/cPickle.*.so ${libdir}/python3.3/pickletools.* "

DESCRIPTION_${PN}-pkgutil="Python Package Extension Utility Support"
RDEPENDS_${PN}-pkgutil="${PN}-core"
FILES_${PN}-pkgutil="${libdir}/python3.3/pkgutil.* "

DESCRIPTION_${PN}-pprint="Python Pretty-Print Support"
RDEPENDS_${PN}-pprint="${PN}-core"
FILES_${PN}-pprint="${libdir}/python3.3/pprint.* "

DESCRIPTION_${PN}-profile="Python Basic Profiling Support"
RDEPENDS_${PN}-profile="${PN}-core ${PN}-textutils"
FILES_${PN}-profile="${libdir}/python3.3/profile.* ${libdir}/python3.3/pstats.* ${libdir}/python3.3/cProfile.* ${libdir}/python3.3/lib-dynload/_lsprof.*.so "

DESCRIPTION_${PN}-pydoc="Python Interactive Help Support"
RDEPENDS_${PN}-pydoc="${PN}-core ${PN}-lang ${PN}-stringold ${PN}-re"
FILES_${PN}-pydoc="${bindir}/pydoc ${libdir}/python3.3/pydoc.* ${libdir}/python3.3/pydoc_data "

DESCRIPTION_${PN}-re="Python Regular Expression APIs"
RDEPENDS_${PN}-re="${PN}-core"
FILES_${PN}-re="${libdir}/python3.3/re.* ${libdir}/python3.3/sre.* ${libdir}/python3.3/sre_compile.* ${libdir}/python3.3/sre_constants* ${libdir}/python3.3/sre_parse.* "

DESCRIPTION_${PN}-readline="Python Readline Support"
RDEPENDS_${PN}-readline="${PN}-core"
FILES_${PN}-readline="${libdir}/python3.3/lib-dynload/readline.*.so ${libdir}/python3.3/rlcompleter.* "

DESCRIPTION_${PN}-resource="Python Resource Control Interface"
RDEPENDS_${PN}-resource="${PN}-core"
FILES_${PN}-resource="${libdir}/python3.3/lib-dynload/resource.*.so "

DESCRIPTION_${PN}-robotparser="Python robots.txt parser"
RDEPENDS_${PN}-robotparser="${PN}-core ${PN}-netclient"
FILES_${PN}-robotparser="${libdir}/python3.3/urllib/robotparser.* "

DESCRIPTION_${PN}-shell="Python Shell-Like Functionality"
RDEPENDS_${PN}-shell="${PN}-core ${PN}-re"
FILES_${PN}-shell="${libdir}/python3.3/cmd.* ${libdir}/python3.3/commands.* ${libdir}/python3.3/dircache.* ${libdir}/python3.3/fnmatch.* ${libdir}/python3.3/glob.* ${libdir}/python3.3/popen2.* ${libdir}/python3.3/shlex.* ${libdir}/python3.3/shutil.* "

DESCRIPTION_${PN}-smtpd="Python Simple Mail Transport Daemon"
RDEPENDS_${PN}-smtpd="${PN}-core ${PN}-netserver ${PN}-email ${PN}-mime"
FILES_${PN}-smtpd="${bindir}/smtpd.* ${libdir}/python3.3/smtpd.* "

DESCRIPTION_${PN}-sqlite3="Python Sqlite3 Database Support"
RDEPENDS_${PN}-sqlite3="${PN}-core ${PN}-datetime ${PN}-lang ${PN}-crypt ${PN}-io ${PN}-threading"
FILES_${PN}-sqlite3="${libdir}/python3.3/lib-dynload/_sqlite3.*.so ${libdir}/python3.3/sqlite3/dbapi2.* ${libdir}/python3.3/sqlite3/__init__.* ${libdir}/python3.3/sqlite3/dump.* "

DESCRIPTION_${PN}-sqlite3-tests="Python Sqlite3 Database Support Tests"
RDEPENDS_${PN}-sqlite3-tests="${PN}-core ${PN}-sqlite3"
FILES_${PN}-sqlite3-tests="${libdir}/python3.3/sqlite3/test "

DESCRIPTION_${PN}-stringold="Python String APIs [deprecated]"
RDEPENDS_${PN}-stringold="${PN}-core ${PN}-re"
FILES_${PN}-stringold="${libdir}/python3.3/lib-dynload/strop.*.so ${libdir}/python3.3/string.* ${libdir}/python3.3/stringold.* "

DESCRIPTION_${PN}-subprocess="Python Subprocess Support"
RDEPENDS_${PN}-subprocess="${PN}-core ${PN}-io ${PN}-re ${PN}-fcntl ${PN}-pickle"
FILES_${PN}-subprocess="${libdir}/python3.3/subprocess.* "

DESCRIPTION_${PN}-syslog="Python Syslog Interface"
RDEPENDS_${PN}-syslog="${PN}-core"
FILES_${PN}-syslog="${libdir}/python3.3/lib-dynload/syslog.*.so "

DESCRIPTION_${PN}-terminal="Python Terminal Controlling Support"
RDEPENDS_${PN}-terminal="${PN}-core ${PN}-io"
FILES_${PN}-terminal="${libdir}/python3.3/pty.* ${libdir}/python3.3/tty.* "

DESCRIPTION_${PN}-tests="Python Tests"
RDEPENDS_${PN}-tests="${PN}-core"
FILES_${PN}-tests="${libdir}/python3.3/test "

DESCRIPTION_${PN}-textutils="Python Option Parsing, Text Wrapping and Comma-Separated-Value Support"
RDEPENDS_${PN}-textutils="${PN}-core ${PN}-io ${PN}-re ${PN}-stringold"
FILES_${PN}-textutils="${libdir}/python3.3/lib-dynload/_csv.*.so ${libdir}/python3.3/csv.* ${libdir}/python3.3/optparse.* ${libdir}/python3.3/textwrap.* "

DESCRIPTION_${PN}-threading="Python Threading & Synchronization Support"
RDEPENDS_${PN}-threading="${PN}-core ${PN}-lang"
FILES_${PN}-threading="${libdir}/python3.3/_threading_local.* ${libdir}/python3.3/dummy_thread.* ${libdir}/python3.3/dummy_threading.* ${libdir}/python3.3/mutex.* ${libdir}/python3.3/threading.* ${libdir}/python3.3/Queue.* "

DESCRIPTION_${PN}-tkinter="Python Tcl/Tk Bindings"
RDEPENDS_${PN}-tkinter="${PN}-core"
FILES_${PN}-tkinter="${libdir}/python3.3/lib-dynload/_tkinter.*.so ${libdir}/python3.3/lib-tk ${libdir}/python3.3/tkinter "

DESCRIPTION_${PN}-unittest="Python Unit Testing Framework"
RDEPENDS_${PN}-unittest="${PN}-core ${PN}-stringold ${PN}-lang"
FILES_${PN}-unittest="${libdir}/python3.3/unittest/ "

DESCRIPTION_${PN}-unixadmin="Python Unix Administration Support"
RDEPENDS_${PN}-unixadmin="${PN}-core"
FILES_${PN}-unixadmin="${libdir}/python3.3/lib-dynload/nis.*.so ${libdir}/python3.3/lib-dynload/grp.*.so ${libdir}/python3.3/lib-dynload/pwd.*.so ${libdir}/python3.3/getpass.* "

DESCRIPTION_${PN}-xml="Python basic XML support."
RDEPENDS_${PN}-xml="${PN}-core ${PN}-elementtree ${PN}-re"
FILES_${PN}-xml="${libdir}/python3.3/lib-dynload/pyexpat.*.so ${libdir}/python3.3/xml ${libdir}/python3.3/xmllib.* "

DESCRIPTION_${PN}-xmlrpc="Python XMLRPC Support"
RDEPENDS_${PN}-xmlrpc="${PN}-core ${PN}-xml ${PN}-netserver ${PN}-lang"
FILES_${PN}-xmlrpc="${libdir}/python3.3/xmlrpclib.* ${libdir}/python3.3/SimpleXMLRPCServer.* ${libdir}/python3.3/DocXMLRPCServer.* ${libdir}/python3.3/xmlrpc "

DESCRIPTION_${PN}-modules="All Python modules"
RDEPENDS_${PN}-modules="${PN}-2to3 ${PN}-audio ${PN}-codecs ${PN}-compile ${PN}-compression ${PN}-core ${PN}-crypt ${PN}-ctypes ${PN}-curses ${PN}-datetime ${PN}-db ${PN}-debugger ${PN}-difflib ${PN}-distutils ${PN}-doctest ${PN}-elementtree ${PN}-email ${PN}-fcntl ${PN}-gdbm ${PN}-html ${PN}-idle ${PN}-image ${PN}-io ${PN}-json ${PN}-lang ${PN}-logging ${PN}-mailbox ${PN}-math ${PN}-mime ${PN}-mmap ${PN}-multiprocessing ${PN}-netclient ${PN}-netserver ${PN}-numbers ${PN}-pickle ${PN}-pkgutil ${PN}-pprint ${PN}-profile ${PN}-pydoc ${PN}-re ${PN}-readline ${PN}-resource ${PN}-robotparser ${PN}-shell ${PN}-smtpd ${PN}-sqlite3 ${PN}-sqlite3-tests ${PN}-stringold ${PN}-subprocess ${PN}-syslog ${PN}-terminal ${PN}-tests ${PN}-textutils ${PN}-threading ${PN}-tkinter ${PN}-unittest ${PN}-unixadmin ${PN}-xml ${PN}-xmlrpc  "
ALLOW_EMPTY_${PN}-modules = "1"



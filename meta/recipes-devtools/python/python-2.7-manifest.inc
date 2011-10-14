
# WARNING: This file is AUTO GENERATED: Manual edits will be lost next time I regenerate the file.
# Generator: '../../../scripts/contrib/python/generate-manifest-2.6.py' Version 20110222 (C) 2002-2010 Michael 'Mickey' Lauer <mlauer@vanille-media.de>
# Visit the Python for Embedded Systems Site => http://www.Vanille.de/projects/python.spy

 

PROVIDES+="${PN}-audio ${PN}-bsddb ${PN}-codecs ${PN}-compile ${PN}-compiler ${PN}-compression ${PN}-core ${PN}-crypt ${PN}-ctypes ${PN}-curses ${PN}-datetime ${PN}-db ${PN}-debugger ${PN}-dev ${PN}-difflib ${PN}-distutils ${PN}-doctest ${PN}-elementtree ${PN}-email ${PN}-fcntl ${PN}-gdbm ${PN}-hotshot ${PN}-html ${PN}-idle ${PN}-image ${PN}-io ${PN}-json ${PN}-lang ${PN}-logging ${PN}-mailbox ${PN}-math ${PN}-mime ${PN}-mmap ${PN}-multiprocessing ${PN}-netclient ${PN}-netserver ${PN}-numbers ${PN}-pickle ${PN}-pkgutil ${PN}-pprint ${PN}-profile ${PN}-pydoc ${PN}-re ${PN}-readline ${PN}-resource ${PN}-robotparser ${PN}-shell ${PN}-smtpd ${PN}-sqlite3 ${PN}-sqlite3-tests ${PN}-stringold ${PN}-subprocess ${PN}-syslog ${PN}-terminal ${PN}-tests ${PN}-textutils ${PN}-threading ${PN}-tkinter ${PN}-unittest ${PN}-unixadmin ${PN}-xml ${PN}-xmlrpc ${PN}-zlib "

PACKAGES="${PN}-dbg ${PN}-audio ${PN}-bsddb ${PN}-codecs ${PN}-compile ${PN}-compiler ${PN}-compression ${PN}-core ${PN}-crypt ${PN}-ctypes ${PN}-curses ${PN}-datetime ${PN}-db ${PN}-debugger ${PN}-dev ${PN}-difflib ${PN}-distutils ${PN}-doctest ${PN}-elementtree ${PN}-email ${PN}-fcntl ${PN}-gdbm ${PN}-hotshot ${PN}-html ${PN}-idle ${PN}-image ${PN}-io ${PN}-json ${PN}-lang ${PN}-logging ${PN}-mailbox ${PN}-math ${PN}-mime ${PN}-mmap ${PN}-multiprocessing ${PN}-netclient ${PN}-netserver ${PN}-numbers ${PN}-pickle ${PN}-pkgutil ${PN}-pprint ${PN}-profile ${PN}-pydoc ${PN}-re ${PN}-readline ${PN}-resource ${PN}-robotparser ${PN}-shell ${PN}-smtpd ${PN}-sqlite3 ${PN}-sqlite3-tests ${PN}-stringold ${PN}-subprocess ${PN}-syslog ${PN}-terminal ${PN}-tests ${PN}-textutils ${PN}-threading ${PN}-tkinter ${PN}-unittest ${PN}-unixadmin ${PN}-xml ${PN}-xmlrpc ${PN}-zlib ${PN}-modules"

DESCRIPTION_${PN}-audio="Python Audio Handling"
RDEPENDS_${PN}-audio="${PN}-core"
FILES_${PN}-audio="${libdir}/python2.6/wave.* ${libdir}/python2.6/chunk.* ${libdir}/python2.6/sndhdr.* ${libdir}/python2.6/lib-dynload/ossaudiodev.so ${libdir}/python2.6/lib-dynload/audioop.so "

DESCRIPTION_${PN}-bsddb="Python Berkeley Database Bindings"
RDEPENDS_${PN}-bsddb="${PN}-core"
FILES_${PN}-bsddb="${libdir}/python2.6/bsddb ${libdir}/python2.6/lib-dynload/_bsddb.so "

DESCRIPTION_${PN}-codecs="Python Codecs, Encodings & i18n Support"
RDEPENDS_${PN}-codecs="${PN}-core ${PN}-lang"
FILES_${PN}-codecs="${libdir}/python2.6/codecs.* ${libdir}/python2.6/encodings ${libdir}/python2.6/gettext.* ${libdir}/python2.6/locale.* ${libdir}/python2.6/lib-dynload/_locale.so ${libdir}/python2.6/lib-dynload/unicodedata.so ${libdir}/python2.6/stringprep.* ${libdir}/python2.6/xdrlib.* "

DESCRIPTION_${PN}-compile="Python Bytecode Compilation Support"
RDEPENDS_${PN}-compile="${PN}-core"
FILES_${PN}-compile="${libdir}/python2.6/py_compile.* ${libdir}/python2.6/compileall.* "

DESCRIPTION_${PN}-compiler="Python Compiler Support"
RDEPENDS_${PN}-compiler="${PN}-core"
FILES_${PN}-compiler="${libdir}/python2.6/compiler "

DESCRIPTION_${PN}-compression="Python High Level Compression Support"
RDEPENDS_${PN}-compression="${PN}-core ${PN}-zlib"
FILES_${PN}-compression="${libdir}/python2.6/gzip.* ${libdir}/python2.6/zipfile.* ${libdir}/python2.6/tarfile.* ${libdir}/python2.6/lib-dynload/bz2.so "

DESCRIPTION_${PN}-core="Python Interpreter and core modules (needed!)"
RDEPENDS_${PN}-core=""
FILES_${PN}-core="${libdir}/python2.6/__future__.* ${libdir}/python2.6/_abcoll.* ${libdir}/python2.6/abc.* ${libdir}/python2.6/copy.* ${libdir}/python2.6/copy_reg.* ${libdir}/python2.6/ConfigParser.* ${libdir}/python2.6/genericpath.* ${libdir}/python2.6/getopt.* ${libdir}/python2.6/linecache.* ${libdir}/python2.6/new.* ${libdir}/python2.6/os.* ${libdir}/python2.6/posixpath.* ${libdir}/python2.6/struct.* ${libdir}/python2.6/warnings.* ${libdir}/python2.6/site.* ${libdir}/python2.6/stat.* ${libdir}/python2.6/UserDict.* ${libdir}/python2.6/UserList.* ${libdir}/python2.6/UserString.* ${libdir}/python2.6/lib-dynload/binascii.so ${libdir}/python2.6/lib-dynload/_struct.so ${libdir}/python2.6/lib-dynload/time.so ${libdir}/python2.6/lib-dynload/xreadlines.so ${libdir}/python2.6/types.* ${libdir}/python2.6/platform.* ${bindir}/python* "

DESCRIPTION_${PN}-crypt="Python Basic Cryptographic and Hashing Support"
RDEPENDS_${PN}-crypt="${PN}-core"
FILES_${PN}-crypt="${libdir}/python2.6/hashlib.* ${libdir}/python2.6/md5.* ${libdir}/python2.6/sha.* ${libdir}/python2.6/lib-dynload/crypt.so ${libdir}/python2.6/lib-dynload/_hashlib.so ${libdir}/python2.6/lib-dynload/_sha256.so ${libdir}/python2.6/lib-dynload/_sha512.so "

DESCRIPTION_${PN}-ctypes="Python C Types Support"
RDEPENDS_${PN}-ctypes="${PN}-core"
FILES_${PN}-ctypes="${libdir}/python2.6/ctypes ${libdir}/python2.6/lib-dynload/_ctypes.so "

DESCRIPTION_${PN}-curses="Python Curses Support"
RDEPENDS_${PN}-curses="${PN}-core"
FILES_${PN}-curses="${libdir}/python2.6/curses ${libdir}/python2.6/lib-dynload/_curses.so ${libdir}/python2.6/lib-dynload/_curses_panel.so "

DESCRIPTION_${PN}-datetime="Python Calendar and Time support"
RDEPENDS_${PN}-datetime="${PN}-core ${PN}-codecs"
FILES_${PN}-datetime="${libdir}/python2.6/_strptime.* ${libdir}/python2.6/calendar.* ${libdir}/python2.6/lib-dynload/datetime.so "

DESCRIPTION_${PN}-db="Python File-Based Database Support"
RDEPENDS_${PN}-db="${PN}-core"
FILES_${PN}-db="${libdir}/python2.6/anydbm.* ${libdir}/python2.6/dumbdbm.* ${libdir}/python2.6/whichdb.* "

DESCRIPTION_${PN}-debugger="Python Debugger"
RDEPENDS_${PN}-debugger="${PN}-core ${PN}-io ${PN}-lang ${PN}-re ${PN}-stringold ${PN}-shell ${PN}-pprint"
FILES_${PN}-debugger="${libdir}/python2.6/bdb.* ${libdir}/python2.6/pdb.* "

DESCRIPTION_${PN}-dev="Python Development Package"
RDEPENDS_${PN}-dev="${PN}-core"
FILES_${PN}-dev="${includedir} ${libdir}/libpython2.6.so "

DESCRIPTION_${PN}-difflib="Python helpers for computing deltas between objects."
RDEPENDS_${PN}-difflib="${PN}-lang ${PN}-re"
FILES_${PN}-difflib="${libdir}/python2.6/difflib.* "

DESCRIPTION_${PN}-distutils="Python Distribution Utilities"
RDEPENDS_${PN}-distutils="${PN}-core"
FILES_${PN}-distutils="${libdir}/python2.6/config ${libdir}/python2.6/distutils "

DESCRIPTION_${PN}-doctest="Python framework for running examples in docstrings."
RDEPENDS_${PN}-doctest="${PN}-core ${PN}-lang ${PN}-io ${PN}-re ${PN}-unittest ${PN}-debugger ${PN}-difflib"
FILES_${PN}-doctest="${libdir}/python2.6/doctest.* "

DESCRIPTION_${PN}-elementtree="Python elementree"
RDEPENDS_${PN}-elementtree="${PN}-core"
FILES_${PN}-elementtree="${libdir}/python2.6/lib-dynload/_elementtree.so "

DESCRIPTION_${PN}-email="Python Email Support"
RDEPENDS_${PN}-email="${PN}-core ${PN}-io ${PN}-re ${PN}-mime ${PN}-audio ${PN}-image ${PN}-netclient"
FILES_${PN}-email="${libdir}/python2.6/imaplib.* ${libdir}/python2.6/email "

DESCRIPTION_${PN}-fcntl="Python's fcntl Interface"
RDEPENDS_${PN}-fcntl="${PN}-core"
FILES_${PN}-fcntl="${libdir}/python2.6/lib-dynload/fcntl.so "

DESCRIPTION_${PN}-gdbm="Python GNU Database Support"
RDEPENDS_${PN}-gdbm="${PN}-core"
FILES_${PN}-gdbm="${libdir}/python2.6/lib-dynload/gdbm.so "

DESCRIPTION_${PN}-hotshot="Python Hotshot Profiler"
RDEPENDS_${PN}-hotshot="${PN}-core"
FILES_${PN}-hotshot="${libdir}/python2.6/hotshot ${libdir}/python2.6/lib-dynload/_hotshot.so "

DESCRIPTION_${PN}-html="Python HTML Processing"
RDEPENDS_${PN}-html="${PN}-core"
FILES_${PN}-html="${libdir}/python2.6/formatter.* ${libdir}/python2.6/htmlentitydefs.* ${libdir}/python2.6/htmllib.* ${libdir}/python2.6/markupbase.* ${libdir}/python2.6/sgmllib.* "

DESCRIPTION_${PN}-idle="Python Integrated Development Environment"
RDEPENDS_${PN}-idle="${PN}-core ${PN}-tkinter"
FILES_${PN}-idle="${bindir}/idle ${libdir}/python2.6/idlelib "

DESCRIPTION_${PN}-image="Python Graphical Image Handling"
RDEPENDS_${PN}-image="${PN}-core"
FILES_${PN}-image="${libdir}/python2.6/colorsys.* ${libdir}/python2.6/imghdr.* ${libdir}/python2.6/lib-dynload/imageop.so ${libdir}/python2.6/lib-dynload/rgbimg.so "

DESCRIPTION_${PN}-io="Python Low-Level I/O"
RDEPENDS_${PN}-io="${PN}-core ${PN}-math"
FILES_${PN}-io="${libdir}/python2.6/lib-dynload/_socket.so ${libdir}/python2.6/lib-dynload/_ssl.so ${libdir}/python2.6/lib-dynload/select.so ${libdir}/python2.6/lib-dynload/termios.so ${libdir}/python2.6/lib-dynload/cStringIO.so ${libdir}/python2.6/pipes.* ${libdir}/python2.6/socket.* ${libdir}/python2.6/ssl.* ${libdir}/python2.6/tempfile.* ${libdir}/python2.6/StringIO.* "

DESCRIPTION_${PN}-json="Python JSON Support"
RDEPENDS_${PN}-json="${PN}-core ${PN}-math ${PN}-re"
FILES_${PN}-json="${libdir}/python2.6/json "

DESCRIPTION_${PN}-lang="Python Low-Level Language Support"
RDEPENDS_${PN}-lang="${PN}-core"
FILES_${PN}-lang="${libdir}/python2.6/lib-dynload/_bisect.so ${libdir}/python2.6/lib-dynload/_collections.so ${libdir}/python2.6/lib-dynload/_heapq.so ${libdir}/python2.6/lib-dynload/_weakref.so ${libdir}/python2.6/lib-dynload/_functools.so ${libdir}/python2.6/lib-dynload/array.so ${libdir}/python2.6/lib-dynload/itertools.so ${libdir}/python2.6/lib-dynload/operator.so ${libdir}/python2.6/lib-dynload/parser.so ${libdir}/python2.6/atexit.* ${libdir}/python2.6/bisect.* ${libdir}/python2.6/code.* ${libdir}/python2.6/codeop.* ${libdir}/python2.6/collections.* ${libdir}/python2.6/dis.* ${libdir}/python2.6/functools.* ${libdir}/python2.6/heapq.* ${libdir}/python2.6/inspect.* ${libdir}/python2.6/keyword.* ${libdir}/python2.6/opcode.* ${libdir}/python2.6/symbol.* ${libdir}/python2.6/repr.* ${libdir}/python2.6/token.* ${libdir}/python2.6/tokenize.* ${libdir}/python2.6/traceback.* ${libdir}/python2.6/weakref.* "

DESCRIPTION_${PN}-logging="Python Logging Support"
RDEPENDS_${PN}-logging="${PN}-core ${PN}-io ${PN}-lang ${PN}-pickle ${PN}-stringold"
FILES_${PN}-logging="${libdir}/python2.6/logging "

DESCRIPTION_${PN}-mailbox="Python Mailbox Format Support"
RDEPENDS_${PN}-mailbox="${PN}-core ${PN}-mime"
FILES_${PN}-mailbox="${libdir}/python2.6/mailbox.* "

DESCRIPTION_${PN}-math="Python Math Support"
RDEPENDS_${PN}-math="${PN}-core"
FILES_${PN}-math="${libdir}/python2.6/lib-dynload/cmath.so ${libdir}/python2.6/lib-dynload/math.so ${libdir}/python2.6/lib-dynload/_random.so ${libdir}/python2.6/random.* ${libdir}/python2.6/sets.* "

DESCRIPTION_${PN}-mime="Python MIME Handling APIs"
RDEPENDS_${PN}-mime="${PN}-core ${PN}-io"
FILES_${PN}-mime="${libdir}/python2.6/mimetools.* ${libdir}/python2.6/uu.* ${libdir}/python2.6/quopri.* ${libdir}/python2.6/rfc822.* "

DESCRIPTION_${PN}-mmap="Python Memory-Mapped-File Support"
RDEPENDS_${PN}-mmap="${PN}-core ${PN}-io"
FILES_${PN}-mmap="${libdir}/python2.6/lib-dynload/mmap.so "

DESCRIPTION_${PN}-multiprocessing="Python Multiprocessing Support"
RDEPENDS_${PN}-multiprocessing="${PN}-core ${PN}-io ${PN}-lang"
FILES_${PN}-multiprocessing="${libdir}/python2.6/lib-dynload/_multiprocessing.so ${libdir}/python2.6/multiprocessing "

DESCRIPTION_${PN}-netclient="Python Internet Protocol Clients"
RDEPENDS_${PN}-netclient="${PN}-core ${PN}-crypt ${PN}-datetime ${PN}-io ${PN}-lang ${PN}-logging ${PN}-mime"
FILES_${PN}-netclient="${libdir}/python2.6/*Cookie*.* ${libdir}/python2.6/base64.* ${libdir}/python2.6/cookielib.* ${libdir}/python2.6/ftplib.* ${libdir}/python2.6/gopherlib.* ${libdir}/python2.6/hmac.* ${libdir}/python2.6/httplib.* ${libdir}/python2.6/mimetypes.* ${libdir}/python2.6/nntplib.* ${libdir}/python2.6/poplib.* ${libdir}/python2.6/smtplib.* ${libdir}/python2.6/telnetlib.* ${libdir}/python2.6/urllib.* ${libdir}/python2.6/urllib2.* ${libdir}/python2.6/urlparse.* ${libdir}/python2.6/uuid.* ${libdir}/python2.6/rfc822.* ${libdir}/python2.6/mimetools.* "

DESCRIPTION_${PN}-netserver="Python Internet Protocol Servers"
RDEPENDS_${PN}-netserver="${PN}-core ${PN}-netclient"
FILES_${PN}-netserver="${libdir}/python2.6/cgi.* ${libdir}/python2.6/*HTTPServer.* ${libdir}/python2.6/SocketServer.* "

DESCRIPTION_${PN}-numbers="Python Number APIs"
RDEPENDS_${PN}-numbers="${PN}-core ${PN}-lang ${PN}-re"
FILES_${PN}-numbers="${libdir}/python2.6/decimal.* ${libdir}/python2.6/numbers.* "

DESCRIPTION_${PN}-pickle="Python Persistence Support"
RDEPENDS_${PN}-pickle="${PN}-core ${PN}-codecs ${PN}-io ${PN}-re"
FILES_${PN}-pickle="${libdir}/python2.6/pickle.* ${libdir}/python2.6/shelve.* ${libdir}/python2.6/lib-dynload/cPickle.so "

DESCRIPTION_${PN}-pkgutil="Python Package Extension Utility Support"
RDEPENDS_${PN}-pkgutil="${PN}-core"
FILES_${PN}-pkgutil="${libdir}/python2.6/pkgutil.* "

DESCRIPTION_${PN}-pprint="Python Pretty-Print Support"
RDEPENDS_${PN}-pprint="${PN}-core"
FILES_${PN}-pprint="${libdir}/python2.6/pprint.* "

DESCRIPTION_${PN}-profile="Python Basic Profiling Support"
RDEPENDS_${PN}-profile="${PN}-core ${PN}-textutils"
FILES_${PN}-profile="${libdir}/python2.6/profile.* ${libdir}/python2.6/pstats.* ${libdir}/python2.6/cProfile.* ${libdir}/python2.6/lib-dynload/_lsprof.so "

DESCRIPTION_${PN}-pydoc="Python Interactive Help Support"
RDEPENDS_${PN}-pydoc="${PN}-core ${PN}-lang ${PN}-stringold ${PN}-re"
FILES_${PN}-pydoc="${bindir}/pydoc ${libdir}/python2.6/pydoc.* "

DESCRIPTION_${PN}-re="Python Regular Expression APIs"
RDEPENDS_${PN}-re="${PN}-core"
FILES_${PN}-re="${libdir}/python2.6/re.* ${libdir}/python2.6/sre.* ${libdir}/python2.6/sre_compile.* ${libdir}/python2.6/sre_constants* ${libdir}/python2.6/sre_parse.* "

DESCRIPTION_${PN}-readline="Python Readline Support"
RDEPENDS_${PN}-readline="${PN}-core"
FILES_${PN}-readline="${libdir}/python2.6/lib-dynload/readline.so ${libdir}/python2.6/rlcompleter.* "

DESCRIPTION_${PN}-resource="Python Resource Control Interface"
RDEPENDS_${PN}-resource="${PN}-core"
FILES_${PN}-resource="${libdir}/python2.6/lib-dynload/resource.so "

DESCRIPTION_${PN}-robotparser="Python robots.txt parser"
RDEPENDS_${PN}-robotparser="${PN}-core ${PN}-netclient"
FILES_${PN}-robotparser="${libdir}/python2.6/robotparser.* "

DESCRIPTION_${PN}-shell="Python Shell-Like Functionality"
RDEPENDS_${PN}-shell="${PN}-core ${PN}-re"
FILES_${PN}-shell="${libdir}/python2.6/cmd.* ${libdir}/python2.6/commands.* ${libdir}/python2.6/dircache.* ${libdir}/python2.6/fnmatch.* ${libdir}/python2.6/glob.* ${libdir}/python2.6/popen2.* ${libdir}/python2.6/shlex.* ${libdir}/python2.6/shutil.* "

DESCRIPTION_${PN}-smtpd="Python Simple Mail Transport Daemon"
RDEPENDS_${PN}-smtpd="${PN}-core ${PN}-netserver ${PN}-email ${PN}-mime"
FILES_${PN}-smtpd="${bindir}/smtpd.* "

DESCRIPTION_${PN}-sqlite3="Python Sqlite3 Database Support"
RDEPENDS_${PN}-sqlite3="${PN}-core ${PN}-datetime ${PN}-lang ${PN}-crypt ${PN}-io ${PN}-threading ${PN}-zlib"
FILES_${PN}-sqlite3="${libdir}/python2.6/lib-dynload/_sqlite3.so ${libdir}/python2.6/sqlite3/dbapi2.* ${libdir}/python2.6/sqlite3/__init__.* ${libdir}/python2.6/sqlite3/dump.* "

DESCRIPTION_${PN}-sqlite3-tests="Python Sqlite3 Database Support Tests"
RDEPENDS_${PN}-sqlite3-tests="${PN}-core ${PN}-sqlite3"
FILES_${PN}-sqlite3-tests="${libdir}/python2.6/sqlite3/test "

DESCRIPTION_${PN}-stringold="Python String APIs [deprecated]"
RDEPENDS_${PN}-stringold="${PN}-core ${PN}-re"
FILES_${PN}-stringold="${libdir}/python2.6/lib-dynload/strop.so ${libdir}/python2.6/string.* "

DESCRIPTION_${PN}-subprocess="Python Subprocess Support"
RDEPENDS_${PN}-subprocess="${PN}-core ${PN}-io ${PN}-re ${PN}-fcntl ${PN}-pickle"
FILES_${PN}-subprocess="${libdir}/python2.6/subprocess.* "

DESCRIPTION_${PN}-syslog="Python Syslog Interface"
RDEPENDS_${PN}-syslog="${PN}-core"
FILES_${PN}-syslog="${libdir}/python2.6/lib-dynload/syslog.so "

DESCRIPTION_${PN}-terminal="Python Terminal Controlling Support"
RDEPENDS_${PN}-terminal="${PN}-core ${PN}-io"
FILES_${PN}-terminal="${libdir}/python2.6/pty.* ${libdir}/python2.6/tty.* "

DESCRIPTION_${PN}-tests="Python Tests"
RDEPENDS_${PN}-tests="${PN}-core"
FILES_${PN}-tests="${libdir}/python2.6/test "

DESCRIPTION_${PN}-textutils="Python Option Parsing, Text Wrapping and Comma-Separated-Value Support"
RDEPENDS_${PN}-textutils="${PN}-core ${PN}-io ${PN}-re ${PN}-stringold"
FILES_${PN}-textutils="${libdir}/python2.6/lib-dynload/_csv.so ${libdir}/python2.6/csv.* ${libdir}/python2.6/optparse.* ${libdir}/python2.6/textwrap.* "

DESCRIPTION_${PN}-threading="Python Threading & Synchronization Support"
RDEPENDS_${PN}-threading="${PN}-core ${PN}-lang"
FILES_${PN}-threading="${libdir}/python2.6/_threading_local.* ${libdir}/python2.6/dummy_thread.* ${libdir}/python2.6/dummy_threading.* ${libdir}/python2.6/mutex.* ${libdir}/python2.6/threading.* ${libdir}/python2.6/Queue.* "

DESCRIPTION_${PN}-tkinter="Python Tcl/Tk Bindings"
RDEPENDS_${PN}-tkinter="${PN}-core"
FILES_${PN}-tkinter="${libdir}/python2.6/lib-dynload/_tkinter.so ${libdir}/python2.6/lib-tk "

DESCRIPTION_${PN}-unittest="Python Unit Testing Framework"
RDEPENDS_${PN}-unittest="${PN}-core ${PN}-stringold ${PN}-lang"
FILES_${PN}-unittest="${libdir}/python2.6/unittest.* "

DESCRIPTION_${PN}-unixadmin="Python Unix Administration Support"
RDEPENDS_${PN}-unixadmin="${PN}-core"
FILES_${PN}-unixadmin="${libdir}/python2.6/lib-dynload/nis.so ${libdir}/python2.6/lib-dynload/grp.so ${libdir}/python2.6/lib-dynload/pwd.so ${libdir}/python2.6/getpass.* "

DESCRIPTION_${PN}-xml="Python basic XML support."
RDEPENDS_${PN}-xml="${PN}-core ${PN}-elementtree ${PN}-re"
FILES_${PN}-xml="${libdir}/python2.6/lib-dynload/pyexpat.so ${libdir}/python2.6/xml ${libdir}/python2.6/xmllib.* "

DESCRIPTION_${PN}-xmlrpc="Python XMLRPC Support"
RDEPENDS_${PN}-xmlrpc="${PN}-core ${PN}-xml ${PN}-netserver ${PN}-lang"
FILES_${PN}-xmlrpc="${libdir}/python2.6/xmlrpclib.* ${libdir}/python2.6/SimpleXMLRPCServer.* "

DESCRIPTION_${PN}-zlib="Python zlib Support."
RDEPENDS_${PN}-zlib="${PN}-core"
FILES_${PN}-zlib="${libdir}/python2.6/lib-dynload/zlib.so "

DESCRIPTION_${PN}-modules="All Python modules"
RDEPENDS_${PN}-modules="${PN}-audio ${PN}-bsddb ${PN}-codecs ${PN}-compile ${PN}-compiler ${PN}-compression ${PN}-core ${PN}-crypt ${PN}-ctypes ${PN}-curses ${PN}-datetime ${PN}-db ${PN}-debugger ${PN}-difflib ${PN}-distutils ${PN}-doctest ${PN}-elementtree ${PN}-email ${PN}-fcntl ${PN}-gdbm ${PN}-hotshot ${PN}-html ${PN}-idle ${PN}-image ${PN}-io ${PN}-json ${PN}-lang ${PN}-logging ${PN}-mailbox ${PN}-math ${PN}-mime ${PN}-mmap ${PN}-multiprocessing ${PN}-netclient ${PN}-netserver ${PN}-numbers ${PN}-pickle ${PN}-pkgutil ${PN}-pprint ${PN}-profile ${PN}-pydoc ${PN}-re ${PN}-readline ${PN}-resource ${PN}-robotparser ${PN}-shell ${PN}-smtpd ${PN}-sqlite3 ${PN}-sqlite3-tests ${PN}-stringold ${PN}-subprocess ${PN}-syslog ${PN}-terminal ${PN}-tests ${PN}-textutils ${PN}-threading ${PN}-tkinter ${PN}-unittest ${PN}-unixadmin ${PN}-xml ${PN}-xmlrpc ${PN}-zlib  "
ALLOW_EMPTY_${PN}-modules = "1"



# resulttool - compare test durations between two or more runs
#
# SPDX-License-Identifier: GPL-2.0-only
#

import os
import re
import resulttool.resultutils as resultutils

# Matches the section titles "resulttool report" prints before each
# Recipe/Passed/Failed/Skipped/Time(s) table (see template/test_report_full_text.txt)
REPORT_SECTION_RE = re.compile(
    r'^\S+ (?:PTest Result Summary \(Libc: [^)]+\)|Ltp Test Result Summary|Ltp Posix Result Summary)$')
REPORT_ROW_RE = re.compile(
    r'^(?P<name>\S.*?)\s*\|\s*\d+\s*\|\s*\d+\s*\|\s*\d+\s*\|\s*(?P<duration>\d+(?:\.\d+)?)\s*T?\s*$')

def parse_report_log(path):
    """Parse the Recipe/Time(s) tables out of a 'resulttool report' text log"""
    durations = {}
    section = None
    with open(path) as f:
        for line in f:
            line = line.rstrip('\n')
            if REPORT_SECTION_RE.match(line.strip()):
                section = line.strip()
                continue
            if not section:
                continue
            m = REPORT_ROW_RE.match(line)
            if m:
                durations.setdefault(section, {})[m.group('name').strip()] = float(m.group('duration'))
    return durations

def load_durations(source):
    if os.path.isfile(source):
        with open(source, errors='ignore') as f:
            head = f.read(512).lstrip()
        if not head.startswith('{'):
            return parse_report_log(source)
    return get_durations(resultutils.load_resultsdata(source, configmap=resultutils.store_map))

def get_durations(results):
    """Flatten a loaded results dict into {testpath: {test_key: duration}}"""
    durations = {}
    for path, _, _, result in resultutils.test_run_results(results):
        d = durations.setdefault(path, {})
        for k, v in result.items():
            if not isinstance(v, dict):
                continue
            if k.endswith(".sections"):
                # ptestresult.sections/ltpresult.sections/ltpposixresult.sections:
                # duration is per suite, not per testcase
                for suite, info in v.items():
                    dur = info.get('duration') if isinstance(info, dict) else None
                    if dur is None:
                        continue
                    if isinstance(dur, str):
                        dur = dur.split()[0]
                    try:
                        d['%s.%s' % (k, suite)] = float(dur)
                    except ValueError:
                        continue
            elif 'duration' in v:
                d[k] = float(v['duration'])
    return durations

def build_rows(testkeys, path, alldurations):
    rows = []
    for k in testkeys:
        vals = [d[path][k] for d in alldurations]
        # one delta/pct per consecutive pair: (run1,run2), (run2,run3), ...
        deltas = []
        for base, target in zip(vals, vals[1:]):
            delta = target - base
            # a percentage between two runs that both round to 0s is meaningless noise
            if round(base) == 0 and round(target) == 0:
                pct = None
            else:
                pct = (delta / base * 100) if base else 0.0
            deltas.append((delta, pct))
        rows.append((k, vals, deltas))
    return rows

def filter_sort_rows(rows, args):
    """Apply --threshold/--sort-by-delta/--limit to a table's rows"""
    if args.threshold:
        rows = [r for r in rows if any(pct is not None and abs(pct) >= args.threshold for _, pct in r[2])]

    sortkey = (lambda r: max((abs(pct) for _, pct in r[2] if pct is not None), default=0)) if args.sort_by_delta else (lambda r: r[0])
    rows = sorted(rows, key=sortkey, reverse=args.sort_by_delta)
    if args.limit:
        rows = rows[:args.limit]
    return rows

def print_table(title, rows, labels):
    if not rows:
        return

    width = max(len("TESTCASE"), min(70, max(len(r[0]) for r in rows))) + 2
    pair_labels = ["%s->%s" % pair for pair in zip(labels, labels[1:])]
    pair_widths = [max(12, len(pl)) for pl in pair_labels]

    print("\n=== %s ===" % title)
    print("%-*s%s  %s" % (width, "TESTCASE", "".join("%12s" % l for l in labels),
                          "  ".join("%*s%9s" % (pw, pl, "%") for pw, pl in zip(pair_widths, pair_labels))))
    for k, vals, deltas in rows:
        # round() avoids -0.0 rendering as "-0" for small fractional deltas
        pairstrs = ["%*d%9s" % (pw, round(delta), round(pct) if pct is not None else "")
                   for pw, (delta, pct) in zip(pair_widths, deltas)]
        print("%-*s%s  %s" % (width, k[:70], "".join("%12.0f" % v for v in vals), "  ".join(pairstrs)))

def build_tables(args, logger):
    """
    Compute every (title, rows) table 'durations' prints, already filtered/sorted/limited,
    in the order they should be printed: all normal tables first, then (if --show-short)
    one short-tests table per test configuration.
    Returns (labels, tables), or None if args were invalid.
    """
    if len(args.sources) < 2:
        logger.error("At least two sources are needed to compare durations")
        return None

    labels = args.labels.split(',') if args.labels else ['RUN%d' % (i + 1) for i in range(len(args.sources))]
    if len(labels) != len(args.sources):
        logger.error("Number of --labels must match number of sources")
        return None

    alldurations = [load_durations(s) for s in args.sources]

    testpaths = set.intersection(*(set(d) for d in alldurations))
    if not testpaths:
        return labels, []

    tables = []
    short_tables = []
    for path in sorted(testpaths):
        testkeys = set.intersection(*(set(d[path]) for d in alldurations))
        rows = build_rows(testkeys, path, alldurations)

        long_rows = [r for r in rows if max(r[1]) >= args.min_duration]
        short_rows = [r for r in rows if max(r[1]) < args.min_duration]

        tables.append((path, filter_sort_rows(long_rows, args)))
        if args.show_short:
            short_tables.append(("%s (under %gs, all sources)" % (path, args.min_duration),
                                 filter_sort_rows(short_rows, args)))

    return labels, tables + short_tables

def durations(args, logger):
    result = build_tables(args, logger)
    if result is None:
        return 1
    labels, tables = result
    if not tables:
        print("No common test configurations found between the provided sources")
        return 1

    for title, rows in tables:
        print_table(title, rows, labels)
    return 0

def register_commands(subparsers):
    """Register subcommands from this plugin"""
    parser_build = subparsers.add_parser('durations', help='compare test durations across two or more runs',
                                         description='compare oeqa/ptest test and suite durations across two or '
                                                      'more result sets, highlighting the biggest changes',
                                         group='analysis')
    parser_build.set_defaults(func=durations)
    parser_build.add_argument('sources', nargs='+',
                              help='two or more sources to compare, in the order given: each is either a '
                                   'testresults.json file/directory/URL, or a text log produced by '
                                   '"resulttool report"')
    parser_build.add_argument('--labels', default='',
                              help='comma separated labels for each source (default: RUN1, RUN2, ...)')
    parser_build.add_argument('-s', '--sort-by-delta', action='store_true',
                              help='sort by largest absolute change first (default: sorted by test name)')
    parser_build.add_argument('-t', '--threshold', type=float, default=0.0,
                              help='only show tests where at least one consecutive pair of sources changed '
                                   'by at least this percentage')
    parser_build.add_argument('-l', '--limit', type=int, default=0,
                              help='limit output to this many rows per test configuration (0 = no limit)')
    parser_build.add_argument('-m', '--min-duration', type=float, default=10.0,
                              help='omit tests whose duration was under this many seconds in every source '
                                   '(default: 10, use 0 to disable)')
    parser_build.add_argument('--show-short', action='store_true',
                              help='also print tests below --min-duration, in a separate table')

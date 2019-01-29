#!/usr/bin/env python

import sys
import os
import re
from . import ftools

# A parser that can be used to identify weather a line is a test result or a section statement.
class PtestParser(object):
    def __init__(self):
        self.results = Result()
        self.sections = {}

    def parse(self, logfile):
        test_regex = {}
        test_regex['pass'] = re.compile(r"^PASS:(.+)")
        test_regex['fail'] = re.compile(r"^FAIL:(.+)")
        test_regex['skip'] = re.compile(r"^SKIP:(.+)")

        section_regex = {}
        section_regex['begin'] = re.compile(r"^BEGIN: .*/(.+)/ptest")
        section_regex['end'] = re.compile(r"^END: .*/(.+)/ptest")
        section_regex['duration'] = re.compile(r"^DURATION: (.+)")
        section_regex['exitcode'] = re.compile(r"^ERROR: Exit status is (.+)")
        section_regex['timeout'] = re.compile(r"^TIMEOUT: .*/(.+)/ptest")

        def newsection():
            return { 'name': "No-section", 'log': "" }

        current_section = newsection()

        with open(logfile, errors='replace') as f:
            for line in f:
                result = section_regex['begin'].search(line)
                if result:
                    current_section['name'] = result.group(1)
                    continue

                result = section_regex['end'].search(line)
                if result:
                    if current_section['name'] != result.group(1):
                        bb.warn("Ptest END log section mismatch %s vs. %s" % (current_section['name'], result.group(1)))
                    if current_section['name'] in self.sections:
                        bb.warn("Ptest duplicate section for %s" % (current_section['name']))
                    self.sections[current_section['name']] = current_section
                    del self.sections[current_section['name']]['name']
                    current_section = newsection()
                    continue

                result = section_regex['timeout'].search(line)
                if result:
                    if current_section['name'] != result.group(1):
                        bb.warn("Ptest TIMEOUT log section mismatch %s vs. %s" % (current_section['name'], result.group(1)))
                    current_section['timeout'] = True
                    continue

                for t in ['duration', 'exitcode']:
                    result = section_regex[t].search(line)
                    if result:
                        current_section[t] = result.group(1)
                        continue

                current_section['log'] = current_section['log'] + line 

                for t in test_regex:
                    result = test_regex[t].search(line)
                    if result:
                        self.results.store(current_section['name'], result.group(1), t)

        self.results.sort_tests()
        return self.results, self.sections

class Result(object):

    def __init__(self):
        self.result_dict = {}

    def store(self, section, test, status):
        if not section in self.result_dict:
            self.result_dict[section] = []

        self.result_dict[section].append((test, status))

    # sort tests by the test name(the first element of the tuple), for each section. This can be helpful when using git to diff for changes by making sure they are always in the same order.
    def sort_tests(self):
        for package in self.result_dict:
            sorted_results = sorted(self.result_dict[package], key=lambda tup: tup[0])
            self.result_dict[package] = sorted_results

    # Log the results as files. The file name is the section name and the contents are the tests in that section.
    def log_as_files(self, target_dir, test_status):
        status_regex = re.compile('|'.join(map(str, test_status)))
        if not type(test_status) == type([]):
            raise Exception("test_status should be a list. Got " + str(test_status) + " instead.")
        if not os.path.exists(target_dir):
            raise Exception("Target directory does not exist: %s" % target_dir)

        for section, test_results in self.result_dict.items():
            prefix = ''
            for x in test_status:
                prefix +=x+'.'
            if section:
                prefix += section
            section_file = os.path.join(target_dir, prefix)
            # purge the file contents if it exists
            open(section_file, 'w').close()
            for test_result in test_results:
                (test_name, status) = test_result
                # we log only the tests with status in the test_status list
                match_status = status_regex.search(status)
                if match_status:
                    ftools.append_file(section_file, status + ": " + test_name)

    # Not yet implemented!
    def log_to_lava(self):
        pass

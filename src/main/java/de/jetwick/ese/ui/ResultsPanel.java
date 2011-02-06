/**
 * Copyright (C) 2010 Peter Karich <jetwick_@_pannous_._info>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.jetwick.ese.ui;

import de.jetwick.ese.domain.MyUser;
import de.jetwick.ese.search.MySearch;
import de.jetwick.ese.util.Helper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class ResultsPanel extends Panel {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ListView userView;
    private List<MyUser> users = new ArrayList<MyUser>();
    private String queryMessage;
    private String queryMessageWarn;
    private String query;        
    private String sort;
    private int hitsPerPage;        

    public ResultsPanel(String id) {
        super(id);

        add(new Label("qm", new PropertyModel(this, "queryMessage")));
        add(new Label("qmWarn", new PropertyModel(this, "queryMessageWarn")) {

            @Override
            public boolean isVisible() {
                return queryMessageWarn != null && queryMessageWarn.length() > 0;
            }
        });
        
        add(createSortLink("sortName", MySearch.NAME + " desc"));
        add(createSortLink("sortLatest", MySearch.CREATED_AT + " desc"));
        add(createSortLink("sortOldest", MySearch.CREATED_AT + " asc"));

        userView = new ListView("users", users) {

            @Override
            public void populateItem(final ListItem item) {
                final MyUser user = (MyUser) item.getModelObject();
                String twitterUrl = Helper.TURL + "/" + user.getName();

                String name = user.getName();
                LabeledLink userNameLink = new LabeledLink("userNameLink", name, false) {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        onUserClick(user.getName(), null);
                    }
                };
                item.add(userNameLink);
            }
        };

        add(userView);
    }

    public void onUserClick(String userName, String query) {
    }

    public void onSortClicked(AjaxRequestTarget target, String sortStr) {
    }

    public void clear() {
        users.clear();
        queryMessage = "";
        queryMessageWarn = "";
    }

    public void setQueryMessage(String queryMessage) {
        this.queryMessage = queryMessage;
    }

    public void setQueryMessageWarn(String queryMessageWarn) {
        this.queryMessageWarn = queryMessageWarn;
    }

    public void add(MyUser u) {
        users.add(u);
    }

    public void setQuery(String visibleString) {
        query = visibleString;        
    }

    public void setHitsPerPage(int hits) {
        hitsPerPage = hits;
    }

    public void setSort(String sortString) {
        sort = sortString;
    }

    public AjaxFallbackLink createSortLink(String id, final String sorting) {
        AjaxFallbackLink link = new AjaxFallbackLink(id) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                if (target != null)
                    onSortClicked(target, sorting);
            }
        };
        link.add(new AttributeAppender("class", new Model() {

            @Override
            public Serializable getObject() {
                return sorting.equals(sort) ? "selected" : "";
            }
        }, " "));

        return link;
    }
}

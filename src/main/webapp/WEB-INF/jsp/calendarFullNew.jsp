<style>
.month {
  min-width: 800px;
  height: 100%;
}

.week {
  display: flex;
}

.day {
  background-color: rgba(255,255,255,0.8);
  color: #ddd;
  padding: 10px 0 75px 10px;
  margin: 2px;
  flex: 1 1 0;
}

.day:hover {
  background: red;
}

.week:first-of-type .day:first-of-type {
  margin-left: 43%;
}

.week:last-of-type .day:last-of-type {
  margin-right: 14.3%;
}
</style>

<div class='month'>
  <div class='week'>
    <div class='day'>1</div>
    <div class='day'>2</div>
    <div class='day'>3</div>
    <div class='day'>4</div>
  </div>
  <div class='week'>
    <div class='day'>5</div>
    <div class='day'>6</div>
    <div class='day'>7</div>
    <div class='day'>8</div>
    <div class='day'>9</div>
    <div class='day'>10</div>
    <div class='day'>11</div>
  </div>
  <div class='week'>
    <div class='day'>12</div>
    <div class='day'>13</div>
    <div class='day'>14</div>
    <div class='day'>15</div>
    <div class='day'>16</div>
    <div class='day'>17</div>
    <div class='day'>18</div>
  </div>
  <div class='week'>
    <div class='day'>19</div>
    <div class='day'>20</div>
    <div class='day'>21</div>
    <div class='day'>22</div>
    <div class='day'>23</div>
    <div class='day'>24</div>
    <div class='day'>25</div>
  </div>
  <div class='week'>
    <div class='day'>26</div>
    <div class='day'>27</div>
    <div class='day'>28</div>
    <div class='day'>29</div>
    <div class='day'>30</div>
    <div class='day'>31</div>
  </div>
</div>
